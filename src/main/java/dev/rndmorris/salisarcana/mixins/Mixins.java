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
import dev.rndmorris.salisarcana.config.SalisConfig;

public enum Mixins {

    // spotless:off

    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.infernalFurnaceDupeFix::isEnabled)
        .addMixinClasses("blocks.MixinBlockArcaneFurnace")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.beaconBlockFixSetting::isEnabled)
        .addMixinClasses("blocks.MixinBlockCosmeticSolid")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(
            () -> SalisConfig.bugfixes.candleRendererCrashes.isEnabled()
                && !MixinModCompat.disableBlockCandleFixes)
        .addMixinClasses("blocks.MixinBlockCandleRenderer", "blocks.MixinBlockCandle")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.deadMobsDontAttack::isEnabled)
        .addMixinClasses(
            "entities.MixinEntityTaintacle",
            "entities.MixinEntityEldritchCrab",
            "entities.MixinEntityThaumicSlime")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    INTEGER_INFUSION_MATRIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(
            () -> SalisConfig.bugfixes.integerInfusionMatrixMath.isEnabled()
                && !SalisConfig.features.stabilizerRewrite.isEnabled())
        .addMixinClasses("tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.itemShardColor::isEnabled)
        .addMixinClasses("items.MixinItemShard")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.renderRedstoneFix::isEnabled)
        .addMixinClasses("blocks.MixinBlockCustomOre")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.strictInfusionMatrixInputChecks::isEnabled)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    UN_OREDICT_GOLD_COIN(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.unOredictGoldCoin::isEnabled)
        .addMixinClasses("config.MixinConfigItems_UnOredictGoldCoin")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCI_STAFF_VISUAL_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.staffFocusEffectFix::isEnabled)
        .addMixinClasses("client.fx.beams.MixinFXBeamWand")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_FORBID_SWAP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.focalManipulatorForbidSwaps::isEnabled)
        .addMixinClasses("tiles.MixinTileFocalManipulator_ForbidSwap")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_GHOST_ITEM_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchGhostItemFix::isEnabled)
        .addMixinClasses(
            "items.MixinItemWandCasting_DisableSpendingCheck",
            "tiles.MixinTileMagicWorkbench_GhostItemFix")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_ALLOW_RECHARGE_CRAFTING(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchAllowRechargeCrafting::isEnabled)
        .addMixinClasses("tiles.MixinTileMagicWorkbenchCharger")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_MULTI_CONTAINER(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchMultiContainer::isEnabled)
        .addMixinClasses("container.MixinContainerArcaneWorkbench_MultiContainer")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NEGATIVE_BOSS_SPAWN_COUNT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.negativeBossSpawnCount::isEnabled)
        .addMixinClasses("tiles.MixinTileEldritchLock")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    WARP_FAKE_PLAYER(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.warpFakePlayerCheck::isEnabled)
        .addMixinClasses("common.MixinThaumcraft_FakePlayerWarp")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    CRIMSON_RITES_FAKE_PLAYER(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.crimsonRitesFakePlayerCheck::isEnabled)
        .addMixinClasses("items.MixinItemEldritchObject_FakePlayerFix")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCUS_TRADE_BREAK_BLOCKS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.equalTradeBreaksBlocks::isEnabled)
        .addMixinClasses("items.MixinItemFocusTrade_BreakBlocks")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NODE_RECHARGE_TIME(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.nodesRechargeInGameTime::isEnabled)
        .addMixinClasses("tiles.MixinTileNode_RechargeTime")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NODE_REMEMBER_DRAINED(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.nodesRememberBeingDrained::isEnabled)
        .addMixinClasses("tiles.MixinTileNode_RememberUpdates")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ITEM_ICON_METADATA_PROTECTIONS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.bugfixes.itemMetadataSafetyCheck::isEnabled)
        .addMixinClasses("items.Mixin_ItemIconFix", "items.MixinItemWandRod")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SILVERWOOD_LOG_NAME_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.silverwoodLogCorrectName::isEnabled)
        .addMixinClasses("blocks.MixinBlockMagicalLogItem")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    UPDATE_BIOME_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.bugfixes.updateBiomeColorRendering::isEnabled)
        .addMixinClasses("lib.MixinUtils_UpdateBiomeColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RUNED_STONE_CREATIVE_IMMUNITY(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.runedStoneIgnoreCreative::isEnabled)
        .addMixinClasses("tiles.MixinTileEldritchTrap_CreativeImmunity")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    JAR_NO_CREATIVE_DROPS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.jarNoCreativeDrops::isEnabled)
        .addMixinClasses("blocks.MixinBlockJar_NoCreativeDrops")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BANNER_NO_CREATIVE_DROPS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.bannerNoCreativeDrops::isEnabled)
        .addMixinClasses("blocks.MixinBlockWoodenDevice_NoBannerCreativeDrops")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BANNER_PICK_BLOCK(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.bugfixes.bannerPickBlock::isEnabled)
        .addMixinClasses("blocks.MixinBlockWoodenDevice_BannerPickBlock")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    // Features
    EXTENDED_BAUBLES_SUPPORT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.useAllBaublesSlots::isEnabled)
        .addMixinClasses("events.MixinEventHandlerRunic", "items.MixinWandManager", "lib.MixinWarpEvents_BaubleSlots")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.features.useAllBaublesSlots::isEnabled)
        .addMixinClasses("gui.MixinREHWandHandler")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.suppressWarpEventsInCreative::isEnabled)
        .addMixinClasses("events.MixinEventHandlerEntity")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.features.nomiconScrollwheelEnabled::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.features.nomiconShowResearchId::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser_ShowResearchID")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIGATION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.features.nomiconRightClickClose::isEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser_RightClickClose", "gui.MixinGuiResearchRecipe")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.nodeModifierWeights::isEnabled)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NODE_GENERATION_TYPE_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.nodeTypeWeights::isEnabled)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.stabilizerRewrite::isEnabled)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(() -> SalisConfig.features.wandPedestalUseCV.isEnabled() && !MixinModCompat.disableWandCV)
        .addMixinClasses("tiles.MixinTileWandPedestal")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ITEM_ELDRITCH_OBJECT_STACK_SIZE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.itemEldritchObjectStackSize::isEnabled)
        .addMixinClasses("items.MixinItemEldritchObject")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_ITEM_CONSUMPTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.stopCreativeModeItemConsumption::isEnabled)
        .addMixinClasses("blocks.MixinBlockEldritch", "items.MixinItemEssence")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_VIS_CONSUMPTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.infiniteCreativeVis::isEnabled)
        .addMixinClasses("items.MixinItemWandCasting")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    MANA_POD_GROWTH_RATE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.manaPodGrowthRate::isEnabled)
        .addMixinClasses("blocks.MixinBlockManaPod")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_TAB_COMPLETION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.thaumcraftCommandTabCompletion::isEnabled)
        .addMixinClasses("events.MixinCommandThaumcraft_TabCompletion")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_WARP_ARG_ALL(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.thaumcraftCommandWarpArgAll::isEnabled)
        .addMixinClasses("events.MixinCommandThaumcraft_WarpArg")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_SCAN_CONTAINERS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.thaumometerScanContainers::isEnabled)
        .addMixinClasses("items.MixinItemThaumometer", "lib.MixinScanManager")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_OP_THAUMONOMICON(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.creativeOpThaumonomicon::isEnabled)
        .addMixinClasses("lib.MixinResearchManager", "gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    CREATIVE_NO_XP_MANIPULATOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.creativeNoXPManipulator::isEnabled)
        .addMixinClasses("tiles.MixinTileFocalManipulator_NoXP", "gui.MixinGuiFocalManipulator_CreativeNoXP")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_STORE_XP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(() -> SalisConfig.features.enableFocusDisenchanting.isEnabled() || SalisConfig.features.focalDisenchanterReturnXP.isEnabled())
        .addMixinClasses("tiles.MixinTileFocalManipulator_CanStoreXP", "container.MixinContainerFocalManipulator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_RETURN_XP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.focalDisenchanterReturnXP::isEnabled)
        .addMixinClasses("tiles.MixinTileFocalManipulator_CancelReturnXP")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    FOCUS_DISENCHANTING(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.enableFocusDisenchanting::isEnabled)
        .addMixinClasses(
            "tiles.MixinTileFocalManipulator",
            "gui.MixinGuiFocalManipulator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    LEVITATOR_SHIFT_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.levitatorShiftFix::isEnabled)
        .addMixinClasses("tiles.MixinTileLifter")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    PURE_NODE_BIOMECHANGE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.pureNodeBiomeChange::isEnabled)
        .addMixinClasses("tiles.MixinTileNode")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    ELDRITCH_ALTAR_EVEN_SPREAD_MOBS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.eldritchAltarSpawningMethod::isEnabled)
        .addMixinClasses("tiles.MixinTileEldritchAltar_SpawnMobs")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    TAINTED_ITEM_DECAY_CHANCE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.taintedItemDecayChance::isEnabled)
        .addMixinClasses("items.MixinItemResource_DecayChance")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    DISABLE_CREATIVE_TAINTED_ITEM_DECAY(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.disableCreativeTaintedItemDecay::isEnabled)
        .addMixinClasses("items.MixinItemResource_DisableCreativeDecay")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    NAMED_STAFFTERS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.staffterNameTooltip::isEnabled)
        .addMixinClasses("items.MixinItemWandCasting_NamedStaffters")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addMixinClasses("container.MixinContainerArcaneWorkbench_SingleWandReplacement")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT_CLIENT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addMixinClasses("gui.MixinGuiArcaneWorkbench_SingleWandReplacement")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    PRIMAL_CRUSHER_OREDICT_COMPAT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.primalCrusherOredict::isEnabled)
        .addMixinClasses("items.PrimalCrusher_StoneOredictCompat")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_FOCUS_HARVEST_LEVEL(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.equalTradeFocusHarvestLevel::isEnabled)
        .addMixinClasses("items.MixinItemFocusTrade_HarvestLevel")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EXCAVATION_FOCUS_HARVEST_LEVEL(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.excavationFocusHarvestLevel::isEnabled)
        .addMixinClasses("items.MixinItemFocusExcavation_HarvestLevel")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_POTENCY_UPGRADE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(() -> SalisConfig.features.potencyModifiesHarvestLevel.isEnabled() && SalisConfig.features.equalTradeFocusHarvestLevel.isEnabled())
        .addMixinClasses("items.MixinItemFocusTrade_AddPotency")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    PRIMAL_CRUSHER_HARVEST_LEVEL(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.crusherHarvestLevel::isEnabled)
        .addMixinClasses("items.MixinItemPrimalCrusher_HarvestLevel")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_CUSTOM_DURATION(new Builder().setApplyIf(SalisConfig.features.thaumometerDuration::isEnabled)
        .addMixinClasses("items.MixinItemThaumometer_CustomDuration")
        .setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    MISSING_RESEARCH_INFUSION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.notifyMissingResearchInfusion::isEnabled)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_MissingResearch")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_CRUCIBLE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.notifyMissingResearchCrucible::isEnabled)
        .addMixinClasses("tiles.MixinTileCrucible_MissingRecipe")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_WORKBENCH(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.notifyMissingResearchWorkbench::isEnabled)
        .addMixinClasses("gui.MixinGuiArcaneWorkbench_MissingResearch", "lib.MixinArcaneSceptreRecipe", "lib.MixinArcaneWandRecipe")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    RESEARCH_ITEM_EXTENDED(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addMixinClasses("api.ResearchItem_Extended")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ITEM_EXTENDED_THAUMIC_TINKERER(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addMixinClasses("addons.ThaumicTinkerer.TTResearchItem_Extended")
        .addTargetedMod(TargetedMod.THAUMIC_TINKERER)),
    RESEARCH_ITEM_EXTENDED_AUTOMAGY(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addMixinClasses("addons.Automagy.ModResearchItem_Extended")
        .addTargetedMod(TargetedMod.AUTOMAGY)),

    CRUCIBLE_HEAT_SOURCES(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.heatSourceOreDict::isEnabled)
        .addMixinClasses("tiles.MixinTileCrucible_HeatSources", "tiles.MixinTileThaumatorium_HeatSources")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    VIS_RELAY_BOX_EXPANSION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.visRelayBoxExpansion::isEnabled)
        .addMixinClasses("tiles.MixinTileVisRelay_ExpandBoundingBox", "items.MixinItemAmuletVis_ExpandBoundingBox")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TICK_RATE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.visAmuletTickRate::isEnabled)
        .addMixinClasses("items.MixinItemAmuletVis_TickRate")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TRANSFER_RATE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.visAmuletTransferRate::isEnabled)
        .addMixinClasses("items.MixinItemAmuletVis_TransferRate")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_FULL_INVENTORY(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.visAmuletCheckInventory::isEnabled)
        .addMixinClasses("items.MixinItemAmuletVis_InventoryCheck")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    MOB_VIS_WHITELIST(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(() -> !SalisConfig.features.mobVisWhitelist.isEnabled() || SalisConfig.features.mobVisDropList.getNonEmpty().length != 0)
        .addMixinClasses("lib.events.MixinEventHandlerEntity")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    DEADLY_GAZE_MOB_CHECK(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setApplyIf(SalisConfig.features.deadlyGazeMobCheck::isEnabled)
        .addMixinClasses("lib.MixinWarpEvents_DeadlyGaze")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ;
    // spotless:on

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
