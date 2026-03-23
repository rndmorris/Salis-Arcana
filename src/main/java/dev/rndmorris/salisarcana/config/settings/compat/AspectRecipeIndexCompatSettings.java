package dev.rndmorris.salisarcana.config.settings.compat;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.mixins.TargetedMod;

public class AspectRecipeIndexCompatSettings extends BaseCompatSetting {

    public final ToggleSetting capReplacementNEIHandler = new ToggleSetting(
        this,
        "capReplacementNEIHandler",
        "Register an NEI handler for the wand cap substitution feature. Requires _enabledreplaceWandCapsResearch to be enabled in salisarcana/enhancements.cfg");

    public final ToggleSetting coreReplacementNEIHandler = new ToggleSetting(
        this,
        "coreReplacementNEIHandler",
        "Register an NEI handler for the wand core substitution feature. Requires _enabledreplaceWandCoreResearch to be enabled in salisarcana/enhancements.cfg");

    public AspectRecipeIndexCompatSettings(IEnabler dependency) {
        super(dependency, TargetedMod.ASPECT_RECIPE_INDEX);
    }
}
