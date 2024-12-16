package dev.rndmorris.tfixins.config.tweaks;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.IntArraySetting;

public class TweaksModule implements IConfigModule {

    private boolean enabled = true;

    public final IntArraySetting nodeModifierWeights;
    public final IntArraySetting nodeTypeWeights;

    public TweaksModule() {
        nodeModifierWeights = new IntArraySetting(
            new WeakReference<>(this),
            ConfigPhase.EARLY,
            "nodeModifierWeights",
            "Node Modifier Worldgen Weights (bright, pale, fading, normal)",
            new int[] { 0, 0, 0, 0 },
            0,
            100).setEnabled(false);
        nodeTypeWeights = new IntArraySetting(
            new WeakReference<>(this),
            ConfigPhase.EARLY,
            "nodeTypeWeights",
            "Node Type Worldgen Weights (unstable, dark, hungry, pure, normal)",
            new int[] { 0, 0, 0, 0, 0 },
            0,
            100).setEnabled(false);
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
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        if (nodeModifierWeights.phase == phase) {
            nodeModifierWeights.loadFromConfiguration(configuration);
        }
        if (nodeTypeWeights.phase == phase) {
            nodeTypeWeights.loadFromConfiguration(configuration);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
