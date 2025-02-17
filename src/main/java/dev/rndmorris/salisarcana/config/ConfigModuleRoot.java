package dev.rndmorris.salisarcana.config;

import java.io.File;
import java.nio.file.Paths;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.modules.BaseConfigModule;
import dev.rndmorris.salisarcana.config.modules.BiomeColorModule;
import dev.rndmorris.salisarcana.config.modules.BugfixesModule;
import dev.rndmorris.salisarcana.config.modules.CommandsModule;
import dev.rndmorris.salisarcana.config.modules.EnhancementsModule;
import dev.rndmorris.salisarcana.config.modules.ModCompatModule;

public class ConfigModuleRoot {

    public static final BiomeColorModule biomeColors;
    public static final BugfixesModule bugfixes;
    public static final CommandsModule commands;
    public static final EnhancementsModule enhancements;
    public static final ModCompatModule modCompat;

    public static boolean enableVersionChecking;

    private static final BaseConfigModule[] modules = new BaseConfigModule[] { biomeColors = new BiomeColorModule(),
        bugfixes = new BugfixesModule(), commands = new CommandsModule(), enhancements = new EnhancementsModule(),
        modCompat = new ModCompatModule(), };

    public static void synchronizeConfiguration(ConfigPhase phase) {
        final var rootConfigFile = Paths.get("config", SalisArcana.MODID + ".cfg")
            .toString();
        final var rootConfig = new Configuration(new File(rootConfigFile));

        enableVersionChecking = rootConfig
            .getBoolean("enableversionChecking", "general", true, "Check for new versions of Salis Arcana on startup");

        for (var module : modules) {

            final var toggleName = String.format("Enable %s module", module.getModuleId());
            final var enabled = rootConfig
                .getBoolean(toggleName, "modules", module.isEnabled(), module.getModuleComment());
            module.setEnabled(enabled);

            if (!enabled) {
                continue;
            }

            final var moduleConfig = getModuleConfig(module);
            module.loadModuleFromConfig(moduleConfig, phase);

            if (moduleConfig.hasChanged()) {
                moduleConfig.save();
            }
        }

        if (rootConfig.hasChanged()) {
            rootConfig.save();
        }
    }

    private static Configuration getModuleConfig(BaseConfigModule module) {
        final var path = Paths.get("config", SalisArcana.MODID, module.getModuleId() + ".cfg")
            .toString();
        return new Configuration(new File(path));
    }
}
