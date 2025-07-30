package dev.rndmorris.salisarcana.config.group;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.IntSetting;

public class ConfigTweaks extends ConfigGroup {

    private static final String potionCategory = "potion_ids";
    private static final String potionIdComment = "Override the Id of the %s potion effect.";

    public final IntSetting taintPoisonId = new IntSetting(
        this,
        "taintPoisonId",
        String.format(potionIdComment, "Taint Poison"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting fluxFluId = new IntSetting(
        this,
        "fluxFluId",
        String.format(potionIdComment, "Flux Flu"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting fluxPhageId = new IntSetting(
        this,
        "fluxPhageId",
        String.format(potionIdComment, "Flux Phage"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting unnaturalHungerId = new IntSetting(
        this,
        "unnaturalHungerId",
        String.format(potionIdComment, "Unnatural Hunger"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting warpWardId = new IntSetting(
        this,
        "warpWardId",
        String.format(potionIdComment, "Warp Ward"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting deadlyGazeId = new IntSetting(
        this,
        "deadlyGazeId",
        String.format(potionIdComment, "Deadly Gaze"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting blurredVisionId = new IntSetting(
        this,
        "blurredVisionId",
        String.format(potionIdComment, "Blurred Vision"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting sunScornedId = new IntSetting(
        this,
        "sunScornedId",
        String.format(potionIdComment, "Sun Scorned"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);
    public final IntSetting thaumarhiaId = new IntSetting(
        this,
        "thaumarhiaId",
        String.format(potionIdComment, "Thaumarhia"),
        -1).setMinValue(-1)
            .setCategory(potionCategory);

    @Override
    public @NotNull String getGroupName() {
        return "tweaks";
    }

    @Override
    public @NotNull String getGroupComment() {
        return "Miscellaneous tweaks, adjustments, and customizations";
    }

    @Override
    protected Collection<CategoryComment> getCategoryComments() {
        return Collections.singletonList(
            new CategoryComment(
                potionCategory,
                "Override the Ids of Thaumcraft's potion effects. An effect whose Id is set to -1 here will be\nautomatically assigned as normal.\nNOTE: Do not set any Id to 128 or higher unless using a mod that raises the Id limit."));
    }
}
