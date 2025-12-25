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

    private static final String nodeBehaviorsCategory = "node_behaviors";

    private static final String primalArrowCategory = "primal_arrows";

    //
    // Node Behaviors
    //

    public final ToggleSetting hungryDynamicReach = new ToggleSetting(this, "hungryDynamicReach", """
        If true, the radius within which hungry nodes can break blocks and attract entities will scale with the current
        amount of vis of the node.""").setCategory(nodeBehaviorsCategory)
        .setEnabled(false);

    public final ToggleSetting hungryModifierSpeed = new ToggleSetting(this, "hungryModifierSpeed", """
        If true, the rate at which hungry nodes will try to break blocks will be adjusted by their modifier: 20% more
        often when bright, 20% less often when pale, 50% less often when fading.""").setCategory(nodeBehaviorsCategory);

    public final ToggleSetting pureDynamicReach = new ToggleSetting(this, "pureDynamicReach", """
        If true, the radius within which pure nodes will convert their surroundings to Magical Forest will scale with
        the current amount of vis of the node. Small pure nodes that are embedded in silverwood logs will have roughly
        the same range as with this setting disabled.""").setCategory(nodeBehaviorsCategory)
        .setEnabled(false);

    public final ToggleSetting pureModifierSpeed = new ToggleSetting(this, "pureModifierSpeed", """
        If true, the rate at which pure nodes will convert their surroundings to Magical Forest will be adjusted by
        their modifier: 20% more often when bright, 20% less often when pale, 50% less often when fading.""")
        .setCategory(nodeBehaviorsCategory);

    public final ToggleSetting sinisterDynamicReach = new ToggleSetting(this, "sinisterDynamicReach", """
        If true, the radius within which sinister nodes will convert their surroundings to the Eerie biome will scale
        with the current amount of vis of the node.""").setCategory(nodeBehaviorsCategory)
        .setEnabled(false);

    public final ToggleSetting sinisterModifierSpeed = new ToggleSetting(this, "sinisterModifierSpeed", """
        If true, the rate at which sinister nodes will convert their surroundings to the Eerie biome and attempt to
        spawn furious zombies will be adjusted by their modifier: 20% more often when bright, 20% less often when pale,
        50% less often when fading.""").setCategory(nodeBehaviorsCategory);

    public final ToggleSetting taintedDynamicReach = new ToggleSetting(this, "taintedDynamicReach", """
        If true, the radius within which tainted nodes will convert their surroundings to Tainted Lands and spawn taint
        tendrils will scale with the current amount of vis of the node.""").setCategory(nodeBehaviorsCategory)
        .setEnabled(false);

    public final ToggleSetting taintedModifierSpeed = new ToggleSetting(this, "taintedModifierSpeed", """
        If true, the rate at which tainted nodes will convert their surroundings to Tainted Lands and spawn taint
        tendrils will be adjusted by their modifier: 20% more often when bright, 20% less often when pale, 50% less
        often when fading.""").setCategory(nodeBehaviorsCategory);

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

    public final ToggleSetting elementalPickScanTags = new ToggleSetting(this, "elementalPickOredictFilter", """
        When enabled, the Pickaxe of the Core will respect two new ore dictionary tags. Blocks labeled with
        `salisarcana:elementalPickScanExclude` will never be detected by the pickaxe, while blocks labeled with
        `salisarcana:elementalPickScanInclude` will be detected even if they normally would not.""")
        .setCategory("tools");

    public final ToggleSetting primalArrowsCanBeFiredFromDispensers = new ToggleSetting(
        this,
        "canBeFiredFromDispensers",
        "Primal arrows can be shot from dispensers").setCategory(primalArrowCategory);

    public final ToggleSetting hiddenResearchUseWorldRandom = new ToggleSetting(
        this,
        "hiddenResearchUseWorldRandom",
        "When right-clicking a Research Note made from Knowledge Fragments, will use a random value not dependent on the world's time of day.");

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
        return Collections.singletonList(new CategoryComment(potionCategory, """
            Override the ids of Thaumcraft's potion effects. An id not overridden, or that cannot be assigned to its
            overridden id, here will be automatically assigned to the lowest unclaimed id as normal.

            WARNING: Do not set any of these values to 128 or higher unless you are using a mod that increases the
            maximum potion id. If you are, and would like to set these ids to 128 or higher, set
            `_uncapped_potion_ids` to `true`."""));
    }
}
