package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Collections;
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

import org.apache.commons.lang3.text.WordUtils;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class BlockItemListSetting extends Setting {

    public final String comment;
    public final String name;
    private ListType listType = ListType.BOTH;
    private final List<String> defaults = new ArrayList<>();

    private final Set<ParsedEntry> entriesToResolve = new HashSet<>();
    private final List<BlockEntry> blockEntries = new ArrayList<>();
    private final List<ItemEntry> itemEntries = new ArrayList<>();
    private boolean resolved = false;

    public BlockItemListSetting(IEnabler dependency, ConfigPhase phase, String name, String comment) {
        super(dependency, phase);
        this.name = name;
        this.comment = comment;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var enabledOptionName = "enable" + WordUtils.capitalize(name);
        enabled = configuration.getBoolean(enabledOptionName, getCategory(), enabled, "Enable " + name);

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
        for (var entry : blockEntries) {
            if (entry.isMatch(block, metadata)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMatch(Item item, int metadata) {
        if (!resolved) {
            resolveAllEntries();
        }
        for (var entry : itemEntries) {
            if (entry.isMatch(item, metadata)) {
                return true;
            }
        }
        return false;
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
            final var blockEntry = resolveBlock(entry);
            if (blockEntry != null) {
                blockEntries.add(blockEntry);
                continue;
            }
            final var itemEntry = resolveItem(entry);
            if (itemEntry != null) {
                itemEntries.add(itemEntry);
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
            final var blockEntry = resolveBlock(entry);
            if (blockEntry != null) {
                blockEntries.add(blockEntry);
                continue;
            }
            LOG.error("Could not locate a block with full id \"{}\".", entry.getFullId());
        }
    }

    private void resolveItemList() {
        if (resolved) {
            return;
        }
        for (var entry : entriesToResolve) {
            final var itemEntry = resolveItem(entry);
            if (itemEntry != null) {
                itemEntries.add(itemEntry);
                continue;
            }
            LOG.error("Could not locate an item with full id \"{}\".", entry.getFullId());
        }
    }

    private BlockEntry resolveBlock(ParsedEntry entry) {
        final var block = Block.getBlockFromName(entry.getFullId());
        if (block == null) {
            return null;
        }
        return new BlockEntry(block, entry.metadata);
    }

    private ItemEntry resolveItem(ParsedEntry entry) {
        final var registeredItem = Item.itemRegistry.getObject(entry.getFullId());
        if (!(registeredItem instanceof Item item)) {
            return null;
        }
        return new ItemEntry(item, entry.metadata);
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
        public int metadata = -1;

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

            // beyond this point, everything is optional (even if it doesn't parse correctly)

            if (parts.length > 2) {
                final var metadata = IntegerHelper.tryParse(parts[2]);
                if (metadata != null) {
                    result.metadata = metadata;
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

    private static class BlockEntry {

        private final Block block;
        private final int metadata;

        public BlockEntry(Block block, int metadata) {
            this.block = block;
            this.metadata = metadata;
        }

        public boolean isMatch(@Nonnull Block block, int metadata) {
            if (this.block != block) {
                return false;
            }
            if (this.metadata < 0) {
                return true;
            }
            return this.metadata == metadata;
        }
    }

    private static class ItemEntry {

        private final Item item;
        private final int metadata;

        public ItemEntry(Item item, int metadata) {
            this.item = item;
            this.metadata = metadata;
        }

        public boolean isMatch(@Nonnull Item item, int metadata) {
            if (this.item != item) {
                return false;
            }
            if (this.metadata < 0) {
                return true;
            }
            return this.metadata == metadata;
        }
    }

}
