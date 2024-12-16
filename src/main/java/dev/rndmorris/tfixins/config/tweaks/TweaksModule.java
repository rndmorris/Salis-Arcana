package dev.rndmorris.tfixins.config.tweaks;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.IntArraySetting;

public class TweaksModule implements IConfigModule {

    private boolean enabled = true;

    public final IntArraySetting nodeModifierWeights;
    public final IntArraySetting nodeTypeWeights;

    public TweaksModule() {
        nodeModifierWeights = new IntArraySetting(
            new WeakReference<>(this),
            "nodeModifierWeights",
            "Node Modifier Worldgen Weights (bright, pale, fading, normal)",
            new int[] { 0, 0, 0, 0 },
            0,
            100,
            false);
        nodeTypeWeights = new IntArraySetting(
            new WeakReference<>(this),
            "nodeTypeWeights",
            "Node Type Worldgen Weights (unstable, dark, hungry, pure, normal)",
            new int[] { 0, 0, 0, 0, 0 },
            0,
            100,
            false);
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "tweaks";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Miscellaneous Tweaks";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        nodeModifierWeights.loadFromConfiguration(configuration);
        nodeTypeWeights.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
