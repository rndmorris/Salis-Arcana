package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class BlockItemListSetting<DATA> extends Setting {

    public final String comment;
    public final String name;
    private ListType listType = ListType.BOTH;
    private final List<String> defaults = new ArrayList<>();

    private Function<String[], DATA> parser;

    private final Set<ParsedEntry> entriesToResolve = new HashSet<>();
    private final HashMap<Block, Map<Integer, DATA>> blockMap = new HashMap<>();
    private final HashMap<Item, Map<Integer, DATA>> itemMap = new HashMap<>();
    private boolean resolved = false;

    public BlockItemListSetting(IEnabler dependency, String name, String comment) {
        super(dependency);
        this.name = name;
        this.comment = comment;
    }

    public BlockItemListSetting<DATA> withAdditionalData(Function<String[], DATA> parser) {
        this.parser = parser;
        return this;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var rawEntries = configuration
            .getStringList(name, getCategory(), defaults.toArray(new String[0]), comment);

        for (var rawEntry : rawEntries) {
            if (rawEntry.trim()
                .isEmpty()) {
                continue;
            }
            final var entry = parseEntry(rawEntry);
            if (entry == null) {
                LOG.error("Invalid syntax for {} entry \"{}\"", name, rawEntries);
                continue;
            }
            entriesToResolve.add(entry);
        }
    }

    public boolean hasEntry(Block block, int metadata) {
        return getData(block, metadata).containedKeys;
    }

    public boolean hasEntry(Item item, int metadata) {
        return getData(item, metadata).containedKeys;
    }

    public @Nonnull GetDataResult getData(Block block, int metadata) {
        if (!resolved) {
            resolveAllEntries();
        }
        return getData(metadata, blockMap.get(block));
    }

    public @Nonnull GetDataResult getData(Item item, int metadata) {
        if (!resolved) {
            resolveAllEntries();
        }
        return getData(metadata, itemMap.get(item));
    }

    private @Nonnull GetDataResult getData(int metadata, Map<Integer, DATA> dataMap) {
        if (dataMap == null) {
            return new GetDataResult(false, null);
        }
        if (dataMap.containsKey(metadata)) {
            return new GetDataResult(true, dataMap.get(metadata));
        }
        if (dataMap.containsKey(OreDictionary.WILDCARD_VALUE)) {
            return new GetDataResult(true, dataMap.get(OreDictionary.WILDCARD_VALUE));
        }
        return new GetDataResult(false, null);
    }

    private void resolveAllEntries() {
        if (resolved) {
            return;
        }

        switch (listType) {
            case BOTH -> resolveBothList();
            case BLOCKS -> resolveBlockList();
            case ITEMS -> resolveItemList();
        }

        resolved = true;
        entriesToResolve.clear();
    }

    private void resolveBothList() {
        if (resolved) {
            return;
        }
        for (var entry : entriesToResolve) {
            if (tryAddBlock(entry)) {
                continue;
            }
            if (tryAddItem(entry)) {
                continue;
            }
            LOG.error("Could not locate a block or item with full id \"{}\".", entry.getFullId());
        }
    }

    private void resolveBlockList() {
        if (resolved) {
            return;
        }

        for (var entry : entriesToResolve) {
            if (!tryAddBlock(entry)) {
                LOG.error("Could not locate a block with full id \"{}\".", entry.getFullId());
            }
        }
    }

    private void resolveItemList() {
        if (resolved) {
            return;
        }
        for (var entry : entriesToResolve) {
            if (!tryAddItem(entry)) {
                LOG.error("Could not locate an item with full id \"{}\".", entry.getFullId());
            }
        }
    }

    private boolean tryAddBlock(ParsedEntry entry) {
        final var block = resolveBlock(entry);
        if (block == null) {
            return false;
        }
        if (block == Blocks.air) {
            logAirError();
            return false;
        }
        final var dataMap = blockMap.computeIfAbsent(block, k -> new HashMap<>());
        dataMap.put(entry.metadata, entry.data);
        return true;
    }

    private boolean tryAddItem(ParsedEntry entry) {
        final var item = resolveItem(entry);
        if (item == null) {
            return false;
        }

        if (item == Item.getItemFromBlock(Blocks.air)) {
            logAirError();
            return false;
        }

        final var dataMap = itemMap.computeIfAbsent(item, k -> new HashMap<>());
        dataMap.put(entry.metadata, entry.data);
        return true;
    }

    private void logAirError() {
        LOG.error("Attempted to add Air to {}. For technical reasons, this is not allowed.", name);
    }

    private Block resolveBlock(ParsedEntry entry) {
        return Block.getBlockFromName(entry.getFullId());
    }

    private Item resolveItem(ParsedEntry entry) {
        final var registeredItem = Item.itemRegistry.getObject(entry.getFullId());
        if (!(registeredItem instanceof Item item)) {
            return null;
        }
        return item;
    }

    public BlockItemListSetting<DATA> addDefaults(String... defaults) {
        Collections.addAll(this.defaults, defaults);
        return this;
    }

    public BlockItemListSetting<DATA> setListType(@Nonnull ListType listType) {
        this.listType = listType;
        return this;
    }

    public enum ListType {
        BOTH,
        BLOCKS,
        ITEMS;
    }

    private @Nullable ParsedEntry parseEntry(String rawEntry) {
        final var parts = rawEntry.split(":");
        if (parts.length < 2) {
            return null;
        }
        final var result = new ParsedEntry();
        result.modId = parts[0];
        result.id = parts[1];

        if (parts.length > 2) {
            if ("*".equals(parts[2])) {
                result.metadata = OreDictionary.WILDCARD_VALUE;
            } else {
                final var metadata = IntegerHelper.tryParse(parts[2]);
                if (metadata != null) {
                    result.metadata = metadata;
                }
            }
        }

        if (parser != null) {
            result.data = parser.apply(parts);
        }

        return result;
    }

    private class ParsedEntry {

        public String modId;
        public String id;
        public int metadata = 0;
        public DATA data = null;

        public String getFullId() {
            return modId + ":" + id;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            // noinspection unchecked
            ParsedEntry entry = (ParsedEntry) object;
            return metadata == entry.metadata && Objects.equals(modId, entry.modId) && Objects.equals(id, entry.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(modId, id, metadata);
        }
    }

    public class GetDataResult {

        public final boolean containedKeys;
        public final @Nullable DATA data;

        private GetDataResult(boolean containedKeys, DATA data) {
            this.containedKeys = containedKeys;
            this.data = data;
        }
    }
}
