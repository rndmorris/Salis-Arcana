package dev.rndmorris.salisarcana.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.modules.ConfigBugfixes;
import dev.rndmorris.salisarcana.config.modules.ConfigCommands;
import dev.rndmorris.salisarcana.config.modules.ConfigEnhancements;
import dev.rndmorris.salisarcana.config.modules.ConfigModCompat;

public class Config {

    // no modifier, so it's visible within the same package (i.e. to ModuleBase)
    static final List<ConfigBase> modules = new ArrayList<>();

    public static final ConfigBugfixes bugfixes = new ConfigBugfixes();
    public static final ConfigCommands commands = new ConfigCommands();
    public static final ConfigEnhancements enhancements = new ConfigEnhancements();
    public static final ConfigModCompat modCompat = new ConfigModCompat();

    public static boolean enableVersionChecking;

    public static void synchronizeConfiguration() {
        final var rootConfigFile = Paths.get("config", SalisArcana.MODID + ".cfg")
            .toString();
        final var rootConfig = new Configuration(new File(rootConfigFile));

        enableVersionChecking = rootConfig
            .getBoolean("enableversionChecking", "general", true, "Check for new versions of Salis Arcana on startup");

        for (var module : modules) {

            final var toggleName = String.format("Enable %s module", module.getFileName());
            final var enabled = rootConfig
                .getBoolean(toggleName, "modules", module.isEnabled(), module.getFileComment());
            module.setEnabled(enabled);

            if (!enabled) {
                continue;
            }

            final var moduleConfig = getModuleConfig(module);
            module.loadFromConfig(moduleConfig);

            if (moduleConfig.hasChanged()) {
                moduleConfig.save();
            }
        }

        if (rootConfig.hasChanged()) {
            rootConfig.save();
        }
    }

    private static Configuration getModuleConfig(ConfigBase module) {
        final var path = Paths.get("config", SalisArcana.MODID, module.getFileName() + ".cfg")
            .toString();
        return new Configuration(new File(path));
    }
}
