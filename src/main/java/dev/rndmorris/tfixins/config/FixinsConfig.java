package dev.rndmorris.tfixins.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class FixinsConfig {

    public static final BiomeColorModule biomeColorModule = new BiomeColorModule();
    public static final CommandsModule commandsModule = new CommandsModule();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        final var loadModules = new IConfigModule[] { biomeColorModule, commandsModule };

        final var modulesCategory = "00_modules";

        configuration.setCategoryComment(
            modulesCategory,
            "Enable and disable Thaumic Fixins modules. Disabled modules will not generate or read from entries in the config file.");

        for (var module : loadModules) {
            final var toggleName = String.format("Enable %s module", module.getModuleId());
            final var enabled = configuration
                .getBoolean(toggleName, modulesCategory, module.isEnabled(), module.getModuleComment());
            module.setEnabled(enabled);
            if (enabled) {
                module.loadModuleFromConfig(configuration);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

}
