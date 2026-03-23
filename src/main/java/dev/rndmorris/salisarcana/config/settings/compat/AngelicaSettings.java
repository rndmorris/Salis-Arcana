package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.mixins.TargetedMod;

public class AngelicaSettings extends BaseCompatSetting {

    public final ToggleSetting replaceTCFontRenderer = new ToggleSetting(
        this,
        "replaceTCFontRenderer",
        "Replace the Thaumonomicon's font rendering with Angelica's, using settings that should improve the appearance of text.");

    public AngelicaSettings(IEnabler dependency) {
        super(dependency, TargetedMod.ANGELICA);
    }
}
