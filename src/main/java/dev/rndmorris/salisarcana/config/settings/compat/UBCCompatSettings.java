package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.mixins.TargetedMod;

public class UBCCompatSettings extends BaseCompatSetting {

    public final ToggleSetting primalCrusherMinesUBCSlabs = new ToggleSetting(
        this,
        "primalCrusherMinesSlabs",
        "Allow the primal crusher to mine UBC slabs.");

    public UBCCompatSettings(IEnabler dependency) {
        super(dependency, TargetedMod.UNDERGROUND_BIOMES);
    }

}
