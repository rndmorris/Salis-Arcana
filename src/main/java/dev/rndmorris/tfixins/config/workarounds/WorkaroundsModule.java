package dev.rndmorris.tfixins.config.workarounds;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.ToggleSetting;

public class WorkaroundsModule implements IConfigModule {

    public final ToggleSetting enableLookalikePlanks;

    private boolean enabled = true;

    public WorkaroundsModule() {
        enableLookalikePlanks = new ToggleSetting(
            new WeakReference<>(this),
            "enableLookalikePlanks",
            "Add look-a-like greatwood and silverwood planks that behave as normal planks, instead of the weirdness of TC4's planks.");
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "workarounds";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "\"Fixes\" for bugs that aren't true fixes.";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        enableLookalikePlanks.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
