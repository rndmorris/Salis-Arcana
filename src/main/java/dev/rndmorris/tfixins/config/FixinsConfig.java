package dev.rndmorris.tfixins.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.biomes.BiomeColorModule;
import dev.rndmorris.tfixins.config.bugfixes.BugfixesModule;
import dev.rndmorris.tfixins.config.commands.CommandsModule;
import dev.rndmorris.tfixins.config.thaumonomicon.ThaumonomiconModule;
import dev.rndmorris.tfixins.config.tweaks.TweaksModule;
import dev.rndmorris.tfixins.config.workarounds.WorkaroundsModule;

public class FixinsConfig {

    public static final BiomeColorModule biomeColorModule;
    public static final BugfixesModule bugfixesModule;
    public static final CommandsModule commandsModule;
    public static final ThaumonomiconModule researchBrowserModule;
    public static final WorkaroundsModule workaroundsModule;
    public static final TweaksModule tweaksModule;

    private static final IConfigModule[] modules;

    static {
        // spotless:off
            modules = new IConfigModule[] {
            biomeColorModule = new BiomeColorModule(),
            bugfixesModule = new BugfixesModule(),
            commandsModule = new CommandsModule(),
            researchBrowserModule = new ThaumonomiconModule(),
            workaroundsModule = new WorkaroundsModule(),
            tweaksModule = new TweaksModule(),
        };
        // spotless:on
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
                module.loadModuleFromConfig(configuration, phase);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
