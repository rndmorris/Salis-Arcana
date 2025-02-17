// Code here adapted from
// https://github.com/GTNewHorizons/Hodgepodge/blob/master/src/main/java/com/mitchej123/hodgepodge/mixins/Mixins.java
// and therefore under the LGPL-3.0 license.

package dev.rndmorris.salisarcana.mixins;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.salisarcana.common.compat.MixinModCompat;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public enum Mixins {

    // Morpheus
    BIOME_COLOR_EERIE_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eerie.baseColor::isEnabled)
        .addMixinClasses("world.biomes.eerie.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eerie.foliageColor::isEnabled)
        .addMixinClasses("world.biomes.eerie.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eerie.grassColor::isEnabled)
        .addMixinClasses("world.biomes.eerie.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eerie.skyColor::isEnabled)
        .addMixinClasses("world.biomes.eerie.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eerie.waterColorMultiplier::isEnabled)
        .addMixinClasses("world.biomes.eerie.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_ELDRITCH_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eldritch.baseColor::isEnabled)
        .addMixinClasses("world.biomes.eldritch.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eldritch.foliageColor::isEnabled)
        .addMixinClasses("world.biomes.eldritch.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eldritch.grassColor::isEnabled)
        .addMixinClasses("world.biomes.eldritch.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eldritch.skyColor::isEnabled)
        .addMixinClasses("world.biomes.eldritch.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.eldritch.waterColorMultiplier::isEnabled)
        .addMixinClasses("world.biomes.eldritch.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_MAGICAL_FOREST_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.magicalForest.baseColor::isEnabled)
        .addMixinClasses("world.biomes.magicalforest.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.magicalForest.foliageColor::isEnabled)
        .addMixinClasses("world.biomes.magicalforest.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.magicalForest.grassColor::isEnabled)
        .addMixinClasses("world.biomes.magicalforest.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.magicalForest.skyColor::isEnabled)
        .addMixinClasses("world.biomes.magicalforest.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.magicalForest.waterColorMultiplier::isEnabled)
        .addMixinClasses("world.biomes.magicalforest.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_TAINT_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.taint.baseColor::isEnabled)
        .addMixinClasses("world.biomes.taint.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.taint.foliageColor::isEnabled)
        .addMixinClasses("world.biomes.taint.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.taint.grassColor::isEnabled)
        .addMixinClasses("world.biomes.taint.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.taint.skyColor::isEnabled)
        .addMixinClasses("world.biomes.taint.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.biomeColors.taint.waterColorMultiplier::isEnabled)
        .addMixinClasses("world.biomes.taint.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.infernalFurnaceDupeFix::isEnabled)
        .addMixinClasses("blocks.MixinBlockArcaneFurnace")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.beaconBlockFixSetting::isEnabled)
        .addMixinClasses("blocks.MixinBlockCosmeticSolid")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(
            () -> ConfigModuleRoot.bugfixes.candleRendererCrashes.isEnabled()
                && !MixinModCompat.disableBlockCandleFixes)
        .addMixinClasses("blocks.MixinBlockCandleRenderer", "blocks.MixinBlockCandle")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.deadMobsDontAttack::isEnabled)
        .addMixinClasses(
            "entities.MixinEntityTaintacle",
            "entities.MixinEntityEldritchCrab",
            "entities.MixinEntityThaumicSlime")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    INTEGER_INFUSION_MATRIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(
            () -> ConfigModuleRoot.bugfixes.integerInfusionMatrixMath.isEnabled()
                && !ConfigModuleRoot.enhancements.stabilizerRewrite.isEnabled())
        .addMixinClasses("tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.itemShardColor::isEnabled)
        .addMixinClasses("items.MixinItemShard")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.renderRedstoneFix::isEnabled)
        .addMixinClasses("blocks.MixinBlockCustomOre")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.strictInfusionMatrixInputChecks::isEnabled)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    UN_OREDICT_GOLD_COIN(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.unOredictGoldCoin::isEnabled)
        .addMixinClasses("config.MixinConfigItems_UnOredictGoldCoin")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCI_STAFF_VISUAL_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.staffFocusEffectFix::isEnabled)
        .addMixinClasses("client.fx.beams.MixinFXBeamWand")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ARCANE_WORKBENCH_GHOST_ITEM_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(ConfigModuleRoot.bugfixes.arcaneWorkbenchGhostItemFix::isEnabled)
        .addMixinClasses(
            "items.MixinItemWandCasting_DisableSpendingCheck",
            "tiles.MixinTileMagicWorkbench_GhostItemFix")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ARCANE_WORKBENCH_ALLOW_RECHARGE_CRAFTING(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.bugfixes.arcaneWorkbenchAllowRechargeCrafting::isEnabled)
        .addMixinClasses("tiles.MixinTileMagicWorkbenchCharger")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    // Enhancements
    EXTENDED_BAUBLES_SUPPORT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.useAllBaublesSlots::isEnabled)
        .addMixinClasses("events.MixinEventHandlerRunic", "items.MixinWandManager", "lib.MixinWarpEvents")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(ConfigModuleRoot.enhancements.useAllBaublesSlots::isEnabled)
        .addMixinClasses("gui.MixinREHWandHandler")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.suppressWarpEventsInCreative::isEnabled)
        .addMixinClasses("events.MixinEventHandlerEntity")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(ConfigModuleRoot.enhancements.nomiconScrollwheelEnabled::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(ConfigModuleRoot.enhancements.nomiconShowResearchId::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIAGTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(ConfigModuleRoot.enhancements.nomiconRightClickClose::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser", "gui.MixinGuiResearchRecipe")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.nodeModifierWeights::isEnabled)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NODE_GENERATION_TYPE_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.nodeTypeWeights::isEnabled)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.stabilizerRewrite::isEnabled)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(() -> ConfigModuleRoot.enhancements.wandPedestalUseCV.isEnabled() && !MixinModCompat.disableWandCV)
        .addMixinClasses("tiles.MixinTileWandPedestal")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ITEM_ELDRITCH_OBJECT_STACK_SIZE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.itemEldritchObjectStackSize::isEnabled)
        .addMixinClasses("items.MixinItemEldritchObject")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_ITEM_CONSUMPTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.stopCreativeModeItemConsumption::isEnabled)
        .addMixinClasses("blocks.MixinBlockEldritch", "items.MixinItemEssence")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_VIS_CONSUMPTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.infiniteCreativeVis::isEnabled)
        .addMixinClasses("items.MixinItemWandCasting")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    MANA_POD_GROWTH_RATE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.manaPodGrowthRate::isEnabled)
        .addMixinClasses("blocks.MixinBlockManaPod")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_TAB_COMPLETION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.thaumcraftCommandTabCompletion::isEnabled)
        .addMixinClasses("events.MixinCommandThaumcraft_TabCompletion")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_WARP_ARG_ALL(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.thaumcraftCommandWarpArgAll::isEnabled)
        .addMixinClasses("events.MixinCommandThaumcraft_WarpArg")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_SCAN_CONTAINERS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.thaumometerScanContainers::isEnabled)
        .addMixinClasses("items.MixinItemThaumometer", "lib.MixinScanManager")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_OP_THAUMONOMICON(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.creativeOpThaumonomicon::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    LEVITATOR_SHIFT_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.levitatorShiftFix::isEnabled)
        .addMixinClasses("tiles.MixinTileLifter")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    PURE_NODE_BIOMECHANGE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.pureNodeBiomeChange::isEnabled)
        .addMixinClasses("tiles.MixinTileNode")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ELDRITCH_ALTAR_EVEN_SPREAD_MOBS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.eldritchAltarSpawningMethod::isEnabled)
        .addMixinClasses("tiles.MixinTileEldritchAltar_SpawnMobs")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    NAMED_STAFFTERS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.staffterNameTooltip::isEnabled)
        .addMixinClasses("items.MixinItemWandCasting_NamedStaffters")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    PRIMAL_CRUSHER_OREDICT_COMPAT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(ConfigModuleRoot.enhancements.primalCrusherOredict::isEnabled)
        .addMixinClasses("items.PrimalCrusher_StoneOredictCompat")
        .addTargetedMod(TargetedMod.THAUMCRAFT));

    private final List<String> mixinClasses;
    private final List<TargetedMod> targetedMods;
    private final List<TargetedMod> excludedMods;
    private final Supplier<Boolean> setApplyIf;
    private final Phase phase;
    private final Side side;

    Mixins(Builder builder) {
        this.mixinClasses = builder.mixinClasses;
        this.targetedMods = builder.targetedMods;
        this.excludedMods = builder.excludedMods;
        this.setApplyIf = builder.setApplyIf;
        this.phase = builder.phase;
        this.side = builder.side;
        if (this.mixinClasses.isEmpty()) {
            throw new RuntimeException("No mixin class specified for Mixin : " + this.name());
        }
        if (this.targetedMods.isEmpty()) {
            throw new RuntimeException("No targeted mods specified for Mixin : " + this.name());
        }
        if (this.setApplyIf == null) {
            throw new RuntimeException("No new Builder().setApplyIf method specified for Mixin : " + this.name());
        }
        if (this.phase == null) {
            throw new RuntimeException("No Phase specified for Mixin : " + this.name());
        } else if (builder.phaseError) {
            throw new RuntimeException("Trying to define Phase twice for " + this.name());
        }
        if (this.side == null) {
            throw new RuntimeException("No new Builder().setSide(Side function specified for Mixin : " + this.name());
        } else if (builder.sideError) {
            throw new RuntimeException("Trying to define Side twice for " + this.name());
        }
    }

    public static List<String> getEarlyMixins(Set<String> loadedCoreMods) {
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.phase == Phase.EARLY) {
                if (mixin.shouldLoad(loadedCoreMods, Collections.emptySet())) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOG.info("Not loading the following EARLY mixins: {}", notLoading.toString());
        return mixins;
    }

    public static List<String> getLateMixins(Set<String> loadedMods) {
        // NOTE: Any targetmod here needs a modid, not a coremod id
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.phase == Phase.LATE) {
                if (mixin.shouldLoad(Collections.emptySet(), loadedMods)) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOG.info("Not loading the following LATE mixins: {}", notLoading.toString());
        return mixins;
    }

    private boolean shouldLoadSide() {
        return side == Side.BOTH || (side == Side.SERVER && FMLLaunchHandler.side()
            .isServer())
            || (side == Side.CLIENT && FMLLaunchHandler.side()
                .isClient());
    }

    private boolean allModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return false;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && !loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && !loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean noModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return true;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean shouldLoad(Set<String> loadedCoreMods, Set<String> loadedMods) {
        return (shouldLoadSide() && setApplyIf.get()
            && allModsLoaded(targetedMods, loadedCoreMods, loadedMods)
            && noModsLoaded(excludedMods, loadedCoreMods, loadedMods));
    }

    private static class Builder {

        private final List<String> mixinClasses = new ArrayList<>();
        private final List<TargetedMod> targetedMods = new ArrayList<>();
        private final List<TargetedMod> excludedMods = new ArrayList<>();
        private Supplier<Boolean> setApplyIf = null;
        private Phase phase = null;
        private Side side = null;
        private boolean sideError, phaseError;

        public Builder addMixinClasses(String... mixinClasses) {
            this.mixinClasses.addAll(Arrays.asList(mixinClasses));
            return this;
        }

        public Builder setPhase(Phase phase) {
            if (this.phase != null) {
                this.phaseError = true;
            }
            this.phase = phase;
            return this;
        }

        public Builder setSide(Side side) {
            if (this.side != null) {
                this.sideError = true;
            }
            this.side = side;
            return this;
        }

        public Builder setApplyIf(Supplier<Boolean> setApplyIf) {
            this.setApplyIf = setApplyIf;
            return this;
        }

        public Builder addTargetedMod(TargetedMod mod) {
            this.targetedMods.add(mod);
            return this;
        }

        public Builder addExcludedMod(TargetedMod mod) {
            this.excludedMods.add(mod);
            return this;
        }
    }

    private enum Side {
        BOTH,
        CLIENT,
        SERVER
    }

    private enum Phase {
        EARLY,
        LATE,
    }
}
