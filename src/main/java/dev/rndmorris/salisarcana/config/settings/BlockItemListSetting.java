package dev.rndmorris.salisarcana.config.settings;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

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

    private final List<Entry> entries = new ArrayList<>();

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
            final var entry = Entry.parse(rawEntry);
            if (entry == null) {
                LOG.error("Invalid syntax for {} entry \"{}\"", name, rawEntries);
                continue;
            }
            entries.add(entry);
        }
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

    private static class Entry {

        public String modId;
        public String blockId;
        public int metadata = -1;

        private static Entry parse(String entry) {
            final var parts = entry.split(":");
            if (parts.length < 2) {
                return null;
            }
            final var result = new Entry();
            result.modId = parts[0];
            result.blockId = parts[1];

            // beyond this point, everything is optional (even if it doesn't parse correctly)

            if (parts.length > 2) {
                final var metadata = IntegerHelper.tryParse(parts[2]);
                if (metadata != null) {
                    result.metadata = metadata;
                }
            }

            return result;
        }

    }

}
