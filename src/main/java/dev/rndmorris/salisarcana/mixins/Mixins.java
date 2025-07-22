package dev.rndmorris.salisarcana.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

import dev.rndmorris.salisarcana.common.compat.MixinModCompat;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.Setting;

public enum Mixins implements IMixins {

    // spotless:off
    // Bugfixes
    ADVANCED_ARCANE_FURNACE_SAVE_NBT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.advAlchemicalFurnaceSaveNbt)
        .addCommonMixins("tiles.MixinTileAlchemyFurnaceAdvanced_PersistNbt")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_FURNACE_DUPE_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.infernalFurnaceDupeFix)
        .addCommonMixins("blocks.MixinBlockArcaneFurnace")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.beaconBlockFixSetting)
        .addCommonMixins("blocks.MixinBlockCosmeticSolid")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.bugfixes.candleRendererCrashes.isEnabled() && !MixinModCompat.disableBlockCandleFixes)
        .addClientMixins(
            "blocks.MixinBlockCandleRenderer",
            "blocks.MixinBlockCandle")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.deadMobsDontAttack)
        .addCommonMixins(
            "entities.MixinEntityTaintacle",
            "entities.MixinEntityEldritchCrab",
            "entities.MixinEntityThaumicSlime")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    INTEGER_INFUSION_MATRIX(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.bugfixes.integerInfusionMatrixMath.isEnabled() && !SalisConfig.features.stabilizerRewrite.isEnabled())
        .addCommonMixins("tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.itemShardColor)
        .addClientMixins("items.MixinItemShard")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.renderRedstoneFix)
        .addCommonMixins("blocks.MixinBlockCustomOre")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.strictInfusionMatrixInputChecks)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UN_OREDICT_GOLD_COIN(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.unOredictGoldCoin)
        .addCommonMixins("config.MixinConfigItems_UnOredictGoldCoin")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCI_STAFF_VISUAL_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.staffFocusEffectFix)
        .addClientMixins("client.fx.beams.MixinFXBeamWand")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_FORBID_SWAP(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.focalManipulatorForbidSwaps)
        .addCommonMixins("tiles.MixinTileFocalManipulator_ForbidSwap")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_GHOST_ITEM_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchGhostItemFix)
        .addClientMixins(
            "items.MixinItemWandCasting_DisableSpendingCheck",
            "tiles.MixinTileMagicWorkbench_GhostItemFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_ALLOW_RECHARGE_CRAFTING(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchAllowRechargeCrafting)
        .addCommonMixins("tiles.MixinTileMagicWorkbenchCharger")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_MULTI_CONTAINER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchMultiContainer)
        .addCommonMixins("container.MixinContainerArcaneWorkbench_MultiContainer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    THAUMATORIUM_MULTI_CONTAINER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.thaumatoriumMultiContainer)
        .addCommonMixins("container.MixinContainerThaumatorium_MultiContainer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NEGATIVE_BOSS_SPAWN_COUNT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.negativeBossSpawnCount)
        .addCommonMixins("tiles.MixinTileEldritchLock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WARP_FAKE_PLAYER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.warpFakePlayerCheck)
        .addCommonMixins("common.MixinThaumcraft_FakePlayerWarp")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CRIMSON_RITES_FAKE_PLAYER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.crimsonRitesFakePlayerCheck)
        .addCommonMixins("items.MixinItemEldritchObject_FakePlayerFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_TRADE_BREAK_BLOCKS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.equalTradeBreaksBlocks)
        .addCommonMixins("items.MixinItemFocusTrade_BreakBlocks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_RECHARGE_TIME(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.nodesRechargeInGameTime)
        .addCommonMixins("tiles.MixinTileNode_RechargeTime")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_REMEMBER_DRAINED(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.nodesRememberBeingDrained)
        .addCommonMixins("tiles.MixinTileNode_RememberUpdates")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_ICON_METADATA_PROTECTIONS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.itemMetadataSafetyCheck)
        .addClientMixins(
            "items.Mixin_ItemIconFix",
            "items.MixinItemWandRod")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SILVERWOOD_LOG_NAME_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.silverwoodLogCorrectName)
        .addCommonMixins("blocks.MixinBlockMagicalLogItem")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UPDATE_BIOME_COLOR(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.updateBiomeColorRendering)
        .addClientMixins("lib.MixinUtils_UpdateBiomeColor")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RUNED_STONE_CREATIVE_IMMUNITY(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.runedStoneIgnoreCreative)
        .addCommonMixins("tiles.MixinTileEldritchTrap_CreativeImmunity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WAND_FOCUS_LEVEL_PATCH(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.upgradedFocusVisCost)
        .addCommonMixins("api.MixinItemFocusBasic_WandUpgradeLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_NO_CREATIVE_DROPS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.jarNoCreativeDrops)
        .addCommonMixins("blocks.MixinBlockJar_NoCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_NO_CREATIVE_DROPS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.bannerNoCreativeDrops)
        .addCommonMixins("blocks.MixinBlockWoodenDevice_NoBannerCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_PICK_BLOCK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.bannerPickBlock)
        .addCommonMixins("blocks.MixinBlockWoodenDevice_BannerPickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_PICK_BLOCK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.jarPickBlock)
        .addCommonMixins("blocks.MixinBlockJar_PickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_COUNTING_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.correctItemInsertion)
        .addCommonMixins("lib.MixinInventoryUtils_AmountCounting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ETHEREALBLOOM_SAVE_NBT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.etherealBloomSaveNBT)
        .addCommonMixins("tiles.MixinTileEtherealBloom")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SILK_TOUCH_CRYSTALS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.silkTouchCrystalClusters)
        .addCommonMixins("blocks.MixinBlockCrystal")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    LOOT_BLOCK_HITBOX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.lootBlockHitbox)
        .addCommonMixins("blocks.MixinBlockLoot_SetHitbox")
        .addClientMixins("client.renderers.block.MixinBlockLootRenderer_ConserveBlockBounds")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FIX_LOCALIZATION_SIDES(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.fixClientSideLocalization)
        .addCommonMixins(
            "blocks.MixinBlockMetalDevice_LocalizeCorrectly",
            "blocks.MixinBlockMirrorItem_LocalizableText",
            "blocks.MixinBlockStoneDevice_LocalizeCorrectly",
            "blocks.MixinBlockWoodenDevice_LocalizableText",
            "items.MixinItemHandMirror_LocalizeCorrectly",
            "items.MixinItemKey_LocalizeCorrectly",
            "items.MixinItemResearchNotes_LocalizeCorrectly",
            "entities.MixinEntityThaumcraftBosses_LocalizeCorrectly",
            "lib.events.MixinEventHandlerEntity_LocalizeCorrectly",
            "lib.network.MixinPacketPlayerCompleteToServer_LocalizeCorrectly",
            "lib.MixinWarpEvents_LocalizeCorrectly",
            "tiles.MixinTileEldritchLock_LocalizeCorrectly")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    // Features
    EXTENDED_BAUBLES_SUPPORT(new SalisBuilder()
        .applyIf(SalisConfig.features.useAllBaublesSlots)
        .addCommonMixins(
            "events.MixinEventHandlerRunic",
            "items.MixinWandManager",
            "lib.MixinWarpEvents_BaubleSlots")
        .addClientMixins("gui.MixinREHWandHandler")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new SalisBuilder()
        .applyIf(SalisConfig.features.suppressWarpEventsInCreative)
        .addCommonMixins("events.MixinEventHandlerEntity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconScrollwheelEnabled)
        .addClientMixins("gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconShowResearchId)
        .addClientMixins("gui.MixinGuiResearchBrowser_ShowResearchID")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIGATION(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconRightClickClose)
        .addClientMixins(
            "gui.MixinGuiResearchBrowser_RightClickClose",
            "gui.MixinGuiResearchRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new SalisBuilder()
        .applyIf(SalisConfig.features.nodeModifierWeights)
        .addCommonMixins("world.MixinThaumcraftWorldGenerator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_GENERATION_TYPE_WEIGHTS(new SalisBuilder()
        .applyIf(SalisConfig.features.nodeTypeWeights)
        .addCommonMixins("world.MixinThaumcraftWorldGenerator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new SalisBuilder()
        .applyIf(SalisConfig.features.stabilizerRewrite)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.wandPedestalUseCV.isEnabled() && !MixinModCompat.disableWandCV)
        .addCommonMixins("tiles.MixinTileWandPedestal")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ITEM_ELDRITCH_OBJECT_STACK_SIZE(new SalisBuilder()
        .applyIf(SalisConfig.features.itemEldritchObjectStackSize)
        .addCommonMixins("items.MixinItemEldritchObject")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_ITEM_CONSUMPTION(new SalisBuilder()
        .applyIf(SalisConfig.features.stopCreativeModeItemConsumption)
        .addCommonMixins(
            "blocks.MixinBlockEldritch",
            "items.MixinItemEssence")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_VIS_CONSUMPTION(new SalisBuilder()
        .applyIf(SalisConfig.features.infiniteCreativeVis)
        .addCommonMixins("items.MixinItemWandCasting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MANA_POD_GROWTH_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.manaPodGrowthRate)
        .addCommonMixins("blocks.MixinBlockManaPod")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_TAB_COMPLETION(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumcraftCommandTabCompletion)
        .addCommonMixins("events.MixinCommandThaumcraft_TabCompletion")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_WARP_ARG_ALL(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumcraftCommandWarpArgAll)
        .addCommonMixins("events.MixinCommandThaumcraft_WarpArg")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_SCAN_CONTAINERS(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumometerScanContainers)
        .addCommonMixins(
            "items.MixinItemThaumometer",
            "lib.MixinScanManager")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_OP_THAUMONOMICON(new SalisBuilder()
        .applyIf(SalisConfig.features.creativeOpThaumonomicon)
        .addCommonMixins("lib.MixinResearchManager")
        .addClientMixins("gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CREATIVE_NO_XP_MANIPULATOR(new SalisBuilder()
        .applyIf(SalisConfig.features.creativeNoXPManipulator)
        .addCommonMixins("tiles.MixinTileFocalManipulator_NoXP")
        .addClientMixins("gui.MixinGuiFocalManipulator_CreativeNoXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_STORE_XP(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.enableFocusDisenchanting.isEnabled() || SalisConfig.features.focalDisenchanterReturnXP.isEnabled())
        .addCommonMixins(
            "tiles.MixinTileFocalManipulator_CanStoreXP",
            "container.MixinContainerFocalManipulator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_RETURN_XP(new SalisBuilder()
        .applyIf(SalisConfig.features.focalDisenchanterReturnXP)
        .addCommonMixins("tiles.MixinTileFocalManipulator_CancelReturnXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_DISENCHANTING(new SalisBuilder()
        .applyIf(SalisConfig.features.enableFocusDisenchanting)
        .addCommonMixins("tiles.MixinTileFocalManipulator")
        .addClientMixins("gui.MixinGuiFocalManipulator")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    LEVITATOR_SHIFT_FIX(new SalisBuilder()
        .applyIf(SalisConfig.features.levitatorShiftFix)
        .addCommonMixins("tiles.MixinTileLifter")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PURE_NODE_BIOMECHANGE(new SalisBuilder()
        .applyIf(SalisConfig.features.pureNodeBiomeChange)
        .addCommonMixins("tiles.MixinTileNode")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ELDRITCH_ALTAR_EVEN_SPREAD_MOBS(new SalisBuilder()
        .applyIf(SalisConfig.features.eldritchAltarSpawningMethod)
        .addCommonMixins("tiles.MixinTileEldritchAltar_SpawnMobs")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    TAINTED_ITEM_DECAY_CHANCE(new SalisBuilder()
        .applyIf(SalisConfig.features.taintedItemDecayChance)
        .addCommonMixins("items.MixinItemResource_DecayChance")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DISABLE_CREATIVE_TAINTED_ITEM_DECAY(new SalisBuilder()
        .applyIf(SalisConfig.features.disableCreativeTaintedItemDecay)
        .addCommonMixins("items.MixinItemResource_DisableCreativeDecay")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NAMED_STAFFTERS(new SalisBuilder()
        .applyIf(SalisConfig.features.staffterNameTooltip)
        .addCommonMixins("items.MixinItemWandCasting_NamedStaffters")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT(new SalisBuilder()
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addCommonMixins("container.MixinContainerArcaneWorkbench_SingleWandReplacement")
        .addClientMixins("gui.MixinGuiArcaneWorkbench_SingleWandReplacement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PRIMAL_CRUSHER_OREDICT_COMPAT(new SalisBuilder()
        .applyIf(SalisConfig.features.primalCrusherOredict)
        .addCommonMixins("items.PrimalCrusher_StoneOredictCompat")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_FOCUS_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.equalTradeFocusHarvestLevel)
        .addCommonMixins("items.MixinItemFocusTrade_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXCAVATION_FOCUS_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.excavationFocusHarvestLevel)
        .addCommonMixins("items.MixinItemFocusExcavation_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_POTENCY_UPGRADE(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.potencyModifiesHarvestLevel.isEnabled() && SalisConfig.features.equalTradeFocusHarvestLevel.isEnabled())
        .addCommonMixins("items.MixinItemFocusTrade_AddPotency")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PRIMAL_CRUSHER_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.crusherHarvestLevel)
        .addCommonMixins("items.MixinItemPrimalCrusher_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_CUSTOM_DURATION(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumometerDuration)
        .addCommonMixins("items.MixinItemThaumometer_CustomDuration")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MISSING_RESEARCH_INFUSION(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchInfusion)
        .addCommonMixins("tiles.MixinTileInfusionMatrix_MissingResearch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_CRUCIBLE(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchCrucible)
        .addCommonMixins("tiles.MixinTileCrucible_MissingRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_WORKBENCH(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchWorkbench)
        .addCommonMixins("lib.MixinArcaneSceptreRecipe", "lib.MixinArcaneWandRecipe")
        .addClientMixins("gui.MixinGuiArcaneWorkbench_MissingResearch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    RESEARCH_ITEM_EXTENDED(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("api.ResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ITEM_EXTENDED_THAUMIC_TINKERER(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("addons.ThaumicTinkerer.TTResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMIC_TINKERER)),
    RESEARCH_ITEM_EXTENDED_AUTOMAGY(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("addons.Automagy.ModResearchItem_Extended")
        .addRequiredMod(TargetedMod.AUTOMAGY)),

    CRUCIBLE_HEAT_SOURCES(new SalisBuilder()
        .applyIf(SalisConfig.features.heatSourceOreDict)
        .addCommonMixins(
            "tiles.MixinTileCrucible_HeatSources",
            "tiles.MixinTileThaumatorium_HeatSources")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    VIS_RELAY_BOX_EXPANSION(new SalisBuilder()
        .applyIf(SalisConfig.features.visRelayBoxExpansion)
        .addCommonMixins(
            "tiles.MixinTileVisRelay_ExpandBoundingBox",
            "items.MixinItemAmuletVis_ExpandBoundingBox")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TICK_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletTickRate)
        .addCommonMixins("items.MixinItemAmuletVis_TickRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TRANSFER_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletTransferRate)
        .addCommonMixins("items.MixinItemAmuletVis_TransferRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_FULL_INVENTORY(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletCheckInventory)
        .addCommonMixins("items.MixinItemAmuletVis_InventoryCheck")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MOB_VIS_WHITELIST(new SalisBuilder()
        .setApplyIf(() -> !SalisConfig.features.mobVisWhitelist.isEnabled() || SalisConfig.features.mobVisDropList.getNonEmpty().length != 0)
        .addCommonMixins("lib.events.MixinEventHandlerEntity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    DEADLY_GAZE_MOB_CHECK(new SalisBuilder()
        .applyIf(SalisConfig.features.deadlyGazeMobCheck)
        .addCommonMixins("lib.MixinWarpEvents_DeadlyGaze")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    DUPLICATION_BUTTON(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconDuplicateResearch)
        .addClientMixins("gui.MixinGuiResearchBrowser_DuplicateButton")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FREE_DUPLICATES(new SalisBuilder()
        .applyIf(SalisConfig.features.researchDuplicationFree)
        .addCommonMixins("tiles.MixinTileResearchTable_FreeDuplicates")
        .addClientMixins("gui.MixinGuiResearchTable_FreeDuplicates")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    FOCUS_POUCH_SLOT(new SalisBuilder()
        .applyIf(SalisConfig.modCompat.baublesExpanded.focusPouchSlot)
        .addCommonMixins("items.MixinItemFocusPouchBauble_ExpandedBaublesSlot")
        .addRequiredMod(TargetedMod.THAUMCRAFT)
        .addRequiredMod(TargetedMod.BAUBLES_EXPANDED))
    ;
    // spotless:on

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder.setPhase(Phase.LATE);
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }

    static class SalisBuilder extends MixinBuilder {

        public MixinBuilder applyIf(Setting config) {
            return super.setApplyIf(config::isEnabled);
        }
    }
}
