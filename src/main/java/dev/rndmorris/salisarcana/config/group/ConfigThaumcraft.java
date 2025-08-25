package dev.rndmorris.salisarcana.config.group;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class ConfigThaumcraft extends ConfigGroup {

    private static final String potionCategory = "potion_ids";
    private static final String potionIdComment = "Override the id of the %s potion effect.";

    private static final String primalArrowCategory = "primal_arrows";

    //
    // Potion Ids
    //

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

    public boolean anyPotionIdOverrideActive() {
        return Arrays
            .stream(
                new IntSetting[] { taintPoisonId, fluxFluId, fluxPhageId, unnaturalHungerId, warpWardId, deadlyGazeId,
                    blurredVisionId, sunScornedId, thaumarhiaId })
            .anyMatch(IntSetting::isEnabled);
    }

    public final ToggleSetting primalArrowsCanBeFiredFromDispensers = new ToggleSetting(
        this,
        "canBeFiredFromDispensers",
        "Primal arrows can be shot from dispensers").setCategory(primalArrowCategory);

    @Override
    public @NotNull String getGroupName() {
        return "thaumcraft_configuration";
    }

    @Override
    public @NotNull String getGroupComment() {
        return "Additional configuration options for Thaumcraft";
    }

    @Override
    protected Collection<CategoryComment> getCategoryComments() {
        return Collections.singletonList(
            new CategoryComment(
                potionCategory,
                "Override the ids of Thaumcraft's potion effects. An id not overridden, or that cannot be assigned to its\noverridden id, here will be automatically assigned to the lowest unclaimed id as normal.\n\nWARNING: Do not set any of these values to 128 or higher unless you are using a mod that increases the\nmaximum potion id. If you are, and would like to set these ids to 128 or higher, set\n`_uncapped_potion_ids` to `true`."));
    }
}
