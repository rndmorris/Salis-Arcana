package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.Config;
import dev.rndmorris.salisarcana.config.ConfigBase;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.config.settings.compat.UBCCompatSettings;

public class ConfigModCompat extends ConfigBase {

    public final UBCCompatSettings undergroundBiomes = new UBCCompatSettings(this);

    public final ToggleSetting tc4tweakScrollPages = new ToggleSetting(
        Config.enhancements.nomiconScrollwheelEnabled,
        "tc4tweakScrollPages",
        "Whether or not scrolling should scroll to next page of tabs or loop to first tab of the page, in the event that there are too many tabs.")
            .setCategory("tc4tweaks");

    @Nonnull
    @Override
    public String getFileName() {
        return "mod_integrations";
    }

    @Nonnull
    @Override
    public String getFileComment() {
        return "Integrations and compatibility with other mods.";
    }
}
