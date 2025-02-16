package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class UBCCompatSettings extends BaseCompatSetting {

    public final ToggleSetting primalCrusherMinesUBCSlabs;

    public UBCCompatSettings(IEnabler dependency, ConfigPhase phase) {
        super(dependency, phase, "UndergroundBiomes");

        addSettings(
            primalCrusherMinesUBCSlabs = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "primalCrusherMinesSlabs",
                "Allow the primal crusher to mine UBC slabs."));
    }

}
