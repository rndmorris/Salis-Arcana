package dev.rndmorris.salisarcana.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.config.modules.BugfixesModule;
import dev.rndmorris.salisarcana.config.modules.CommandsModule;
import dev.rndmorris.salisarcana.config.modules.EnhancementsModule;
import dev.rndmorris.salisarcana.config.modules.ModCompatModule;

// to-do: rename to something less wordy (like "Config"; this will be its own PR because almost everything touches this
// class
public class ConfigModuleRoot {

    // no modifier, so it's visible within the same package (i.e. to ModuleBase)
    static final List<ModuleBase> modules = new ArrayList<>();

    public static final BugfixesModule bugfixes = new BugfixesModule();
    public static final CommandsModule commands = new CommandsModule();
    public static final EnhancementsModule enhancements = new EnhancementsModule();
    public static final ModCompatModule modCompat = new ModCompatModule();

    public static boolean enableVersionChecking;

    public static void synchronizeConfiguration() {
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
            module.loadModuleFromConfig(moduleConfig);

            if (moduleConfig.hasChanged()) {
                moduleConfig.save();
            }
        }

        if (rootConfig.hasChanged()) {
            rootConfig.save();
        }
    }

    private static Configuration getModuleConfig(ModuleBase module) {
        final var path = Paths.get("config", SalisArcana.MODID, module.getModuleId() + ".cfg")
            .toString();
        return new Configuration(new File(path));
    }
}
