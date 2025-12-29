package dev.rndmorris.salisarcana.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.group.ConfigAddons;
import dev.rndmorris.salisarcana.config.group.ConfigBugfixes;
import dev.rndmorris.salisarcana.config.group.ConfigCommands;
import dev.rndmorris.salisarcana.config.group.ConfigFeatures;
import dev.rndmorris.salisarcana.config.group.ConfigModCompat;
import dev.rndmorris.salisarcana.config.group.ConfigThaumcraft;

public class SalisConfig {

    // no modifier, so it's visible within the same package (i.e. to ConfigGroup)
    static final List<ConfigGroup> groups = new ArrayList<>();

    public static final ConfigAddons addons = new ConfigAddons();
    public static final ConfigBugfixes bugfixes = new ConfigBugfixes();
    public static final ConfigCommands commands = new ConfigCommands();
    public static final ConfigFeatures features = new ConfigFeatures();
    public static final ConfigModCompat modCompat = new ConfigModCompat();
    public static final ConfigThaumcraft thaum = new ConfigThaumcraft();

    public static boolean enableVersionChecking;

    public static void synchronizeConfiguration() {
        final var rootConfigFile = Paths.get("config", SalisArcana.MODID + ".cfg")
            .toString();
        final var rootConfig = new Configuration(new File(rootConfigFile));

        enableVersionChecking = rootConfig
            .getBoolean("enableversionChecking", "general", true, "Check for new versions of Salis Arcana on startup");

        for (var group : groups) {

            final var toggleName = String.format("Enable %s group", group.getGroupName());
            final var enabled = rootConfig
                .getBoolean(toggleName, "modules", group.isEnabled(), group.getGroupComment());
            group.setEnabled(enabled);

            if (!enabled) {
                continue;
            }

            final var groupConfig = getGroupConfig(group);
            group.loadFromConfig(groupConfig);

            if (groupConfig.hasChanged()) {
                groupConfig.save();
            }
        }

        if (rootConfig.hasChanged()) {
            rootConfig.save();
        }
    }

    private static Configuration getGroupConfig(ConfigGroup group) {
        final var path = Paths.get("config", SalisArcana.MODID, group.getGroupName() + ".cfg")
            .toString();
        return new Configuration(new File(path));
    }
}
