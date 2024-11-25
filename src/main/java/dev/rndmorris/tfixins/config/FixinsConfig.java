package dev.rndmorris.tfixins.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class FixinsConfig {

    public static final BiomeColorModule biomeColorModule = new BiomeColorModule();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        final var loadModules = new IConfigModule[] { biomeColorModule };

        for (var module : loadModules) {
            final var toggleName = String.format("Enable %s Module", module.getModuleName());
            final var enabled = configuration
                .getBoolean(toggleName, "modules", module.enabledByDefault(), module.getModuleComment());

            if (enabled) {
                module.loadModuleFromConfig(configuration);
            }
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

}
