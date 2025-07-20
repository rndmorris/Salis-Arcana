package dev.rndmorris.salisarcana.config.group;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.config.settings.compat.BaublesExpandedSettings;
import dev.rndmorris.salisarcana.config.settings.compat.GTNHTCWandsCompatSettings;
import dev.rndmorris.salisarcana.config.settings.compat.UBCCompatSettings;

public class ConfigModCompat extends ConfigGroup {

    public final UBCCompatSettings undergroundBiomes = new UBCCompatSettings(this);
    public final GTNHTCWandsCompatSettings gtnhWands = new GTNHTCWandsCompatSettings(this);
    public final BaublesExpandedSettings baublesExpanded = new BaublesExpandedSettings(this);

    public final ToggleSetting tc4tweakScrollPages = new ToggleSetting(
        SalisConfig.features.nomiconScrollwheelEnabled,
        "tc4tweakScrollPages",
        "Whether or not scrolling should scroll to next page of tabs or loop to first tab of the page, in the event that there are too many tabs.")
            .setCategory("tc4tweaks");

    @Nonnull
    @Override
    public String getGroupName() {
        return "mod_integrations";
    }

    @Nonnull
    @Override
    public String getGroupComment() {
        return "Integrations and compatibility with other mods.";
    }
}
