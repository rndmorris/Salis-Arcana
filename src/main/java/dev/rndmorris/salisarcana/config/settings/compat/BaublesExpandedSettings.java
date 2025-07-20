package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class BaublesExpandedSettings extends BaseCompatSetting {

    public final ToggleSetting focusPouchSlot = new ToggleSetting(
        this,
        "focusPouchSlot",
        "Add a special bauble slot specifically for the Focus Pouch.");

    public BaublesExpandedSettings(IEnabler dependency) {
        super(dependency, "Baubles|Expanded");
    }
}
