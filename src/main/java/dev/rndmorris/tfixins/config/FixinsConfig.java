package dev.rndmorris.tfixins.config;

import java.io.File;
import java.nio.file.Paths;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.config.biomes.BiomeColorModule;
import dev.rndmorris.tfixins.config.bugfixes.BugfixesModule;
import dev.rndmorris.tfixins.config.commands.CommandsModule;
import dev.rndmorris.tfixins.config.enhancements.EnhancementsModule;

public class FixinsConfig {

    public static final BiomeColorModule biomeColorModule;
    public static final BugfixesModule bugfixesModule;
    public static final CommandsModule commandsModule;
    public static final EnhancementsModule enhancements;

    private static final IConfigModule[] modules;

    static {
        // spotless:off
            modules = new IConfigModule[] {
            biomeColorModule = new BiomeColorModule(),
            bugfixesModule = new BugfixesModule(),
            commandsModule = new CommandsModule(),
            enhancements = new EnhancementsModule(),
        };
        // spotless:on
    }

    public static void synchronizeConfiguration(ConfigPhase phase) {
        final var baseConfigPath = Paths.get("config", ThaumicFixins.MODID + ".cfg")
            .toString();
        synchronizeConfiguration(new File(baseConfigPath), phase);
    }

    public static void synchronizeConfiguration(File configFile, ConfigPhase phase) {
        Configuration configuration = new Configuration(configFile);

        final var modulesCategory = "00_modules";

        configuration.setCategoryComment(
            modulesCategory,
            "Enable and disable Thaumic Fixins modules. Disabled modules will not generate or read from entries in the config file.");

        for (var module : modules) {
            final var toggleName = String.format("Enable %s module", module.getModuleId());
            final var enabled = configuration
                .getBoolean(toggleName, modulesCategory, module.isEnabled(), module.getModuleComment());
            module.setEnabled(enabled);
            if (enabled) {
                configuration.setCategoryComment(module.getModuleId(), module.getModuleComment());
                module.loadModuleFromConfig(configuration, phase);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
