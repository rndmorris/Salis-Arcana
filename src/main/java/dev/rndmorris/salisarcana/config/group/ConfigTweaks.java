package dev.rndmorris.salisarcana.config.group;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class ConfigTweaks extends ConfigGroup {

    private static final String potionCategory = "potion_ids";
    private static final String potionIdComment = "Override the Id of the %s potion effect.";

    public final ToggleSetting potionIdLimitRaised = new ToggleSetting(
        this,
        "_uncapped_potion_ids",
        "If true, will allow setting potion ids to 128 and higher.").setEnabled(false)
            .setCategory(potionCategory);

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

    public int[] getPotionIdOverrides() {
        return new int[] { taintPoisonId.getValue(), fluxFluId.getValue(), fluxPhageId.getValue(),
            unnaturalHungerId.getValue(), warpWardId.getValue(), deadlyGazeId.getValue(), blurredVisionId.getValue(),
            sunScornedId.getValue(), thaumarhiaId.getValue() };
    }

    public int maxPotionIdOverride() {
        final var ids = getPotionIdOverrides();
        return Arrays.stream(ids)
            .max()
            .getAsInt();
    }

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
                "Override the ids of Thaumcraft's potion effects. An id not overriden here will be automatically assigned\nto the lowest unclaimed id, as normal.\n\nWARNING: Do not set any of these values to 128 or higher unless you are using a mod that increases the\nmaximum potion id. If you are, and would like to set these ids to 128 or higher, set\n`_uncapped_potion_ids` to `true`."));
    }
}
