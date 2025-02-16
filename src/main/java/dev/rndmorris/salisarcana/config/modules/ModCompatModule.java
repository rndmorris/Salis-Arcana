package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.compat.UBCCompatSettings;

public class ModCompatModule extends BaseConfigModule {

    public final UBCCompatSettings undergroundBiomes;

    public ModCompatModule() {
        addSettings(undergroundBiomes = new UBCCompatSettings(this, ConfigPhase.LATE));
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "mod_integrations";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Integrations and compatibility with other mods.";
    }
}
