package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.config.settings.compat.UBCCompatSettings;

public class ModCompatModule extends BaseConfigModule {

    public final UBCCompatSettings undergroundBiomes;
    public final ToggleSetting tc4tweakScrollPages;

    public ModCompatModule() {
        addSettings(undergroundBiomes = new UBCCompatSettings(this));
        addSettings(
            tc4tweakScrollPages = new ToggleSetting(
                ConfigModuleRoot.enhancements.nomiconScrollwheelEnabled,
                "tc4tweakScrollPages",
                "Whether or not scrolling should scroll to next page or reset to top of page, in the event that there are too many tabs.")
                    .setCategory("tc4tweaks"));
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
