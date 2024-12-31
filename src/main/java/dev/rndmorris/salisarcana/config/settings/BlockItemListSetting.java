package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class BlockItemListSetting extends Setting {

    public final String comment;
    public final String name;
    private ListType listType = ListType.BOTH;
    private final List<String> defaults = new ArrayList<>();

    private final Set<ParsedEntry> entriesToResolve = new HashSet<>();
    private final HashMap<Block, Set<Integer>> blockMap = new HashMap<>();
    private final HashMap<Item, Set<Integer>> itemMap = new HashMap<>();
    private boolean resolved = false;

    public BlockItemListSetting(IEnabler dependency, ConfigPhase phase, String name, String comment) {
        super(dependency, phase);
        this.name = name;
        this.comment = comment;
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
            final var entry = ParsedEntry.parse(rawEntry);
            if (entry == null) {
                LOG.error("Invalid syntax for {} entry \"{}\"", name, rawEntries);
                continue;
            }
            entriesToResolve.add(entry);
        }
    }

    public boolean hasMatch(Block block, int metadata) {
        if (!resolved) {
            resolveAllEntries();
        }
        final var mdSet = blockMap.get(block);
        if (mdSet == null) {
            return false;
        }
        if (mdSet.isEmpty()) {
            return true;
        }
        return mdSet.contains(metadata);
    }

    public boolean hasMatch(Item item, int metadata) {
        if (!resolved) {
            resolveAllEntries();
        }
        final var mdSet = itemMap.get(item);
        if (mdSet == null) {
            return false;
        }
        if (mdSet.isEmpty()) {
            return true;
        }
        return mdSet.contains(metadata);
    }

    public boolean hasMatch(ItemStack itemStack) {
        if (!resolved) {
            resolveAllEntries();
        }
        final var item = itemStack.getItem();
        if (item == null) {
            return false;
        }
        final var block = Block.getBlockFromItem(item);
        // we check for air specifically because `getBlockFromItem` will return air if it doesn't find the block
        if (block != null && block != Blocks.air) {
            return hasMatch(block, itemStack.getItemDamage());
        }
        return hasMatch(item, itemStack.getItemDamage());
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
        final var mdSet = blockMap.computeIfAbsent(block, k -> new HashSet<>());
        if (entry.metadata != OreDictionary.WILDCARD_VALUE) {
            mdSet.add(entry.metadata);
        }
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

        final var mdSet = itemMap.computeIfAbsent(item, k -> new HashSet<>());
        if (entry.metadata != OreDictionary.WILDCARD_VALUE) {
            mdSet.add(entry.metadata);
        }
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

    public BlockItemListSetting addDefaults(String... defaults) {
        Collections.addAll(this.defaults, defaults);
        return this;
    }

    public BlockItemListSetting setListType(@Nonnull ListType listType) {
        this.listType = listType;
        return this;
    }

    public enum ListType {
        BOTH,
        BLOCKS,
        ITEMS;
    }

    private static class ParsedEntry {

        public String modId;
        public String id;
        public int metadata = 0;

        public String getFullId() {
            return modId + ":" + id;
        }

        private static ParsedEntry parse(String entry) {
            final var parts = entry.split(":");
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

            return result;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            ParsedEntry entry = (ParsedEntry) object;
            return metadata == entry.metadata && Objects.equals(modId, entry.modId) && Objects.equals(id, entry.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(modId, id, metadata);
        }
    }
}
