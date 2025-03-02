package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.config.settings.compat.UBCCompatSettings;

public class ModCompatModule extends BaseConfigModule {

    public final UBCCompatSettings undergroundBiomes;
    public final ToggleSetting tcneiplugin;

    public ModCompatModule() {
        addSettings(
            undergroundBiomes = new UBCCompatSettings(this, ConfigPhase.LATE),
            tcneiplugin = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "tcneiplugin",
                "Enable compatibility with the Thaumcraft NEI Plugin mod. Used for (e.g.) registering fake crafting recipes with NEI."));
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
