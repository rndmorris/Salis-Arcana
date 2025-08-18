package dev.rndmorris.salisarcana.config.group;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;

public class ConfigTweaks extends ConfigGroup {

    @Override
    public @NotNull String getGroupName() {
        return "tweaks";
    }

    @Override
    public @NotNull String getGroupComment() {
        return "Tweaks and minor adjustments for Thaumcraft";
    }
}
