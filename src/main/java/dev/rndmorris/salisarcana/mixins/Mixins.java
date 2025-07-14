package dev.rndmorris.salisarcana.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import dev.rndmorris.salisarcana.common.compat.MixinModCompat;
import dev.rndmorris.salisarcana.config.SalisConfig;

public enum Mixins implements IMixins {

    // spotless:off
    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.infernalFurnaceDupeFix::isEnabled)
        .addCommonMixins("blocks.MixinBlockArcaneFurnace")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.beaconBlockFixSetting::isEnabled)
        .addCommonMixins("blocks.MixinBlockCosmeticSolid")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> SalisConfig.bugfixes.candleRendererCrashes.isEnabled() && !MixinModCompat.disableBlockCandleFixes)
        .addCommonMixins(
            "blocks.MixinBlockCandleRenderer",
            "blocks.MixinBlockCandle")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.deadMobsDontAttack::isEnabled)
        .addCommonMixins(
            "entities.MixinEntityTaintacle",
            "entities.MixinEntityEldritchCrab",
            "entities.MixinEntityThaumicSlime")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    INTEGER_INFUSION_MATRIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> SalisConfig.bugfixes.integerInfusionMatrixMath.isEnabled() && !SalisConfig.features.stabilizerRewrite.isEnabled())
        .addCommonMixins("tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.itemShardColor::isEnabled)
        .addCommonMixins("items.MixinItemShard")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.renderRedstoneFix::isEnabled)
        .addCommonMixins("blocks.MixinBlockCustomOre")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.strictInfusionMatrixInputChecks::isEnabled)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UN_OREDICT_GOLD_COIN(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.unOredictGoldCoin::isEnabled)
        .addCommonMixins("config.MixinConfigItems_UnOredictGoldCoin")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCI_STAFF_VISUAL_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.staffFocusEffectFix::isEnabled)
        .addCommonMixins("client.fx.beams.MixinFXBeamWand")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_FORBID_SWAP(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.focalManipulatorForbidSwaps::isEnabled)
        .addCommonMixins("tiles.MixinTileFocalManipulator_ForbidSwap")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_GHOST_ITEM_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchGhostItemFix::isEnabled)
        .addClientMixins(
            "items.MixinItemWandCasting_DisableSpendingCheck",
            "tiles.MixinTileMagicWorkbench_GhostItemFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_ALLOW_RECHARGE_CRAFTING(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchAllowRechargeCrafting::isEnabled)
        .addCommonMixins("tiles.MixinTileMagicWorkbenchCharger")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_MULTI_CONTAINER(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.arcaneWorkbenchMultiContainer::isEnabled)
        .addCommonMixins("container.MixinContainerArcaneWorkbench_MultiContainer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NEGATIVE_BOSS_SPAWN_COUNT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.negativeBossSpawnCount::isEnabled)
        .addCommonMixins("tiles.MixinTileEldritchLock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WARP_FAKE_PLAYER(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.warpFakePlayerCheck::isEnabled)
        .addCommonMixins("common.MixinThaumcraft_FakePlayerWarp")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CRIMSON_RITES_FAKE_PLAYER(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.crimsonRitesFakePlayerCheck::isEnabled)
        .addCommonMixins("items.MixinItemEldritchObject_FakePlayerFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_TRADE_BREAK_BLOCKS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.equalTradeBreaksBlocks::isEnabled)
        .addCommonMixins("items.MixinItemFocusTrade_BreakBlocks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_RECHARGE_TIME(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.nodesRechargeInGameTime::isEnabled)
        .addCommonMixins("tiles.MixinTileNode_RechargeTime")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_REMEMBER_DRAINED(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.nodesRememberBeingDrained::isEnabled)
        .addCommonMixins("tiles.MixinTileNode_RememberUpdates")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_ICON_METADATA_PROTECTIONS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.itemMetadataSafetyCheck::isEnabled)
        .addClientMixins(
            "items.Mixin_ItemIconFix",
            "items.MixinItemWandRod")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SILVERWOOD_LOG_NAME_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.silverwoodLogCorrectName::isEnabled)
        .addCommonMixins("blocks.MixinBlockMagicalLogItem")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UPDATE_BIOME_COLOR(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.updateBiomeColorRendering::isEnabled)
        .addClientMixins("lib.MixinUtils_UpdateBiomeColor")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RUNED_STONE_CREATIVE_IMMUNITY(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.runedStoneIgnoreCreative::isEnabled)
        .addCommonMixins("tiles.MixinTileEldritchTrap_CreativeImmunity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WAND_FOCUS_LEVEL_PATCH(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.upgradedFocusVisCost::isEnabled)
        .addCommonMixins("api.MixinItemFocusBasic_WandUpgradeLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_NO_CREATIVE_DROPS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.jarNoCreativeDrops::isEnabled)
        .addCommonMixins("blocks.MixinBlockJar_NoCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_NO_CREATIVE_DROPS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.bannerNoCreativeDrops::isEnabled)
        .addCommonMixins("blocks.MixinBlockWoodenDevice_NoBannerCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_PICK_BLOCK(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.bannerPickBlock::isEnabled)
        .addCommonMixins("blocks.MixinBlockWoodenDevice_BannerPickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_PICK_BLOCK(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.jarPickBlock::isEnabled)
        .addCommonMixins("blocks.MixinBlockJar_PickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_COUNTING_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.bugfixes.correctItemInsertion::isEnabled)
        .addCommonMixins("lib.MixinInventoryUtils_AmountCounting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    // Features
    EXTENDED_BAUBLES_SUPPORT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.useAllBaublesSlots::isEnabled)
        .addCommonMixins(
            "events.MixinEventHandlerRunic",
            "items.MixinWandManager",
            "lib.MixinWarpEvents_BaubleSlots")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.useAllBaublesSlots::isEnabled)
        .addClientMixins("gui.MixinREHWandHandler")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.suppressWarpEventsInCreative::isEnabled)
        .addCommonMixins("events.MixinEventHandlerEntity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.nomiconScrollwheelEnabled::isEnabled)
        .addClientMixins("gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.nomiconShowResearchId::isEnabled)
        .addClientMixins("gui.MixinGuiResearchBrowser_ShowResearchID")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIGATION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.nomiconRightClickClose::isEnabled)
        .addClientMixins(
            "gui.MixinGuiResearchBrowser_RightClickClose",
            "gui.MixinGuiResearchRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.nodeModifierWeights::isEnabled)
        .addCommonMixins("world.MixinThaumcraftWorldGenerator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_GENERATION_TYPE_WEIGHTS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.nodeTypeWeights::isEnabled)
        .addCommonMixins("world.MixinThaumcraftWorldGenerator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.stabilizerRewrite::isEnabled)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> SalisConfig.features.wandPedestalUseCV.isEnabled() && !MixinModCompat.disableWandCV)
        .addCommonMixins("tiles.MixinTileWandPedestal")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ITEM_ELDRITCH_OBJECT_STACK_SIZE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.itemEldritchObjectStackSize::isEnabled)
        .addCommonMixins("items.MixinItemEldritchObject")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_ITEM_CONSUMPTION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.stopCreativeModeItemConsumption::isEnabled)
        .addCommonMixins(
            "blocks.MixinBlockEldritch",
            "items.MixinItemEssence")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_VIS_CONSUMPTION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.infiniteCreativeVis::isEnabled)
        .addCommonMixins("items.MixinItemWandCasting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MANA_POD_GROWTH_RATE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.manaPodGrowthRate::isEnabled)
        .addCommonMixins("blocks.MixinBlockManaPod")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_TAB_COMPLETION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.thaumcraftCommandTabCompletion::isEnabled)
        .addCommonMixins("events.MixinCommandThaumcraft_TabCompletion")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_WARP_ARG_ALL(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.thaumcraftCommandWarpArgAll::isEnabled)
        .addCommonMixins("events.MixinCommandThaumcraft_WarpArg")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_SCAN_CONTAINERS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.thaumometerScanContainers::isEnabled)
        .addCommonMixins(
            "items.MixinItemThaumometer",
            "lib.MixinScanManager")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_OP_THAUMONOMICON(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.creativeOpThaumonomicon::isEnabled)
        .addCommonMixins(
            "lib.MixinResearchManager",
            "gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CREATIVE_NO_XP_MANIPULATOR(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.creativeNoXPManipulator::isEnabled)
        .addCommonMixins(
            "tiles.MixinTileFocalManipulator_NoXP",
            "gui.MixinGuiFocalManipulator_CreativeNoXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_STORE_XP(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> SalisConfig.features.enableFocusDisenchanting.isEnabled() || SalisConfig.features.focalDisenchanterReturnXP.isEnabled())
        .addCommonMixins(
            "tiles.MixinTileFocalManipulator_CanStoreXP",
            "container.MixinContainerFocalManipulator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_RETURN_XP(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.focalDisenchanterReturnXP::isEnabled)
        .addCommonMixins("tiles.MixinTileFocalManipulator_CancelReturnXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_DISENCHANTING(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.enableFocusDisenchanting::isEnabled)
        .addCommonMixins(
            "tiles.MixinTileFocalManipulator",
            "gui.MixinGuiFocalManipulator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    LEVITATOR_SHIFT_FIX(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.levitatorShiftFix::isEnabled)
        .addCommonMixins("tiles.MixinTileLifter")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PURE_NODE_BIOMECHANGE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.pureNodeBiomeChange::isEnabled)
        .addCommonMixins("tiles.MixinTileNode")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ELDRITCH_ALTAR_EVEN_SPREAD_MOBS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.eldritchAltarSpawningMethod::isEnabled)
        .addCommonMixins("tiles.MixinTileEldritchAltar_SpawnMobs")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    TAINTED_ITEM_DECAY_CHANCE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.taintedItemDecayChance::isEnabled)
        .addCommonMixins("items.MixinItemResource_DecayChance")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DISABLE_CREATIVE_TAINTED_ITEM_DECAY(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.disableCreativeTaintedItemDecay::isEnabled)
        .addCommonMixins("items.MixinItemResource_DisableCreativeDecay")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NAMED_STAFFTERS(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.staffterNameTooltip::isEnabled)
        .addCommonMixins("items.MixinItemWandCasting_NamedStaffters")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addCommonMixins("container.MixinContainerArcaneWorkbench_SingleWandReplacement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT_CLIENT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addClientMixins("gui.MixinGuiArcaneWorkbench_SingleWandReplacement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PRIMAL_CRUSHER_OREDICT_COMPAT(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.primalCrusherOredict::isEnabled)
        .addCommonMixins("items.PrimalCrusher_StoneOredictCompat")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_FOCUS_HARVEST_LEVEL(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.equalTradeFocusHarvestLevel::isEnabled)
        .addCommonMixins("items.MixinItemFocusTrade_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXCAVATION_FOCUS_HARVEST_LEVEL(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.excavationFocusHarvestLevel::isEnabled)
        .addCommonMixins("items.MixinItemFocusExcavation_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_POTENCY_UPGRADE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> SalisConfig.features.potencyModifiesHarvestLevel.isEnabled() && SalisConfig.features.equalTradeFocusHarvestLevel.isEnabled())
        .addCommonMixins("items.MixinItemFocusTrade_AddPotency")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PRIMAL_CRUSHER_HARVEST_LEVEL(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.crusherHarvestLevel::isEnabled)
        .addCommonMixins("items.MixinItemPrimalCrusher_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_CUSTOM_DURATION(new MixinBuilder()
        .setApplyIf(SalisConfig.features.thaumometerDuration::isEnabled)
        .addCommonMixins("items.MixinItemThaumometer_CustomDuration")
        .setPhase(Phase.LATE)
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MISSING_RESEARCH_INFUSION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.notifyMissingResearchInfusion::isEnabled)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_MissingResearch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_CRUCIBLE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.notifyMissingResearchCrucible::isEnabled)
        .addCommonMixins("tiles.MixinTileCrucible_MissingRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_WORKBENCH(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.notifyMissingResearchWorkbench::isEnabled)
        .addCommonMixins(
            "gui.MixinGuiArcaneWorkbench_MissingResearch",
            "lib.MixinArcaneSceptreRecipe",
            "lib.MixinArcaneWandRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    RESEARCH_ITEM_EXTENDED(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addCommonMixins("api.ResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ITEM_EXTENDED_THAUMIC_TINKERER(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addCommonMixins("addons.ThaumicTinkerer.TTResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMIC_TINKERER)),
    RESEARCH_ITEM_EXTENDED_AUTOMAGY(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.researchItemExtensions::isEnabled)
        .addCommonMixins("addons.Automagy.ModResearchItem_Extended")
        .addRequiredMod(TargetedMod.AUTOMAGY)),

    CRUCIBLE_HEAT_SOURCES(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.heatSourceOreDict::isEnabled)
        .addCommonMixins(
            "tiles.MixinTileCrucible_HeatSources",
            "tiles.MixinTileThaumatorium_HeatSources")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    VIS_RELAY_BOX_EXPANSION(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.visRelayBoxExpansion::isEnabled)
        .addCommonMixins(
            "tiles.MixinTileVisRelay_ExpandBoundingBox",
            "items.MixinItemAmuletVis_ExpandBoundingBox")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TICK_RATE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.visAmuletTickRate::isEnabled)
        .addCommonMixins("items.MixinItemAmuletVis_TickRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TRANSFER_RATE(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.visAmuletTransferRate::isEnabled)
        .addCommonMixins("items.MixinItemAmuletVis_TransferRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_FULL_INVENTORY(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.visAmuletCheckInventory::isEnabled)
        .addCommonMixins("items.MixinItemAmuletVis_InventoryCheck")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MOB_VIS_WHITELIST(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(() -> !SalisConfig.features.mobVisWhitelist.isEnabled() || SalisConfig.features.mobVisDropList.getNonEmpty().length != 0)
        .addCommonMixins("lib.events.MixinEventHandlerEntity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    DEADLY_GAZE_MOB_CHECK(new MixinBuilder()
        .setPhase(Phase.LATE)
        .setApplyIf(SalisConfig.features.deadlyGazeMobCheck::isEnabled)
        .addCommonMixins("lib.MixinWarpEvents_DeadlyGaze")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ;
    // spotless:on

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}
