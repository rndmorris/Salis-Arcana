package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class GTNHTCWandsCompatSettings extends BaseCompatSetting {

    public final ToggleSetting coreSwapMaterials = new ToggleSetting(
        this,
        "coreSwapMaterials",
        "Require screws and conductors to swap wand/staff cores.");

    public final ToggleSetting cost = new ToggleSetting(this, "cost", "Use the increased vis costs from GTNHTCWands.");

    public GTNHTCWandsCompatSettings(IEnabler dependency) {
        super(dependency, "gtnhtcwands");
    }
}
