package dev.rndmorris.tfixins.config;

import java.io.File;
import java.nio.file.Paths;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.config.modules.BiomeColorModule;
import dev.rndmorris.tfixins.config.modules.BugfixesModule;
import dev.rndmorris.tfixins.config.modules.CommandsModule;
import dev.rndmorris.tfixins.config.modules.EnhancementsModule;
import dev.rndmorris.tfixins.config.modules.IConfigModule;

public class ConfigModuleRoot {

    public static final BiomeColorModule biomeColorModule;
    public static final BugfixesModule bugfixesModule;
    public static final CommandsModule commandsModule;
    public static final EnhancementsModule enhancements;

    private static final IConfigModule[] modules = new IConfigModule[] { biomeColorModule = new BiomeColorModule(),
        bugfixesModule = new BugfixesModule(), commandsModule = new CommandsModule(),
        enhancements = new EnhancementsModule(), };

    public static void synchronizeConfiguration(ConfigPhase phase) {
        final var rootConfigFile = Paths.get("config", ThaumicFixins.MODID + ".cfg")
            .toString();
        final var rootConfig = new Configuration(new File(rootConfigFile));

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

    private static Configuration getModuleConfig(IConfigModule module) {
        final var path = Paths.get("config", ThaumicFixins.MODID, module.getModuleId() + ".cfg")
            .toString();
        return new Configuration(new File(path));
    }
}
