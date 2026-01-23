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
        .addCommonMixins("thaumcraft.common.tiles.MixinTileAlchemyFurnaceAdvanced_PersistNbt")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_FURNACE_DUPE_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.infernalFurnaceDupeFix)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockArcaneFurnace_DupeFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.beaconBlockFixSetting)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockCosmeticSolid_BeaconBlocks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.bugfixes.candleRendererCrashes.isEnabled() && !MixinModCompat.disableBlockCandleFixes)
        .addClientMixins(
            "thaumcraft.client.renderers.block.MixinBlockCandleRenderer_OOB",
            "thaumcraft.common.blocks.MixinBlockCandle_OOB")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.deadMobsDontAttack)
        .addCommonMixins(
            "thaumcraft.common.entities.monster.MixinEntityTaintacle_NoAttackWhenDead",
            "thaumcraft.common.entities.monster.MixinEntityEldritchCrab_NoAttackWhenDead",
            "thaumcraft.common.entities.monster.MixinEntityThaumicSlime_NoAttackWhenDead")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    INTEGER_INFUSION_MATRIX(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.bugfixes.integerInfusionMatrixMath.isEnabled() && !SalisConfig.features.stabilizerRewrite.isEnabled())
        .addCommonMixins("thaumcraft.common.tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.itemShardColor)
        .addClientMixins("thaumcraft.common.items.MixinItemShard_OOB")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.renderRedstoneFix)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockCustomOre_RedstoneFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.strictInfusionMatrixInputChecks)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UN_OREDICT_GOLD_COIN(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.unOredictGoldCoin)
        .addCommonMixins("thaumcraft.common.config.MixinConfigItems_UnOredictGoldCoin")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCI_STAFF_VISUAL_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.staffFocusEffectFix)
        .addClientMixins("thaumcraft.client.fx.beams.MixinFXBeamWand_FociStaffVisualFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_FORBID_SWAP(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.focalManipulatorForbidSwaps)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileFocalManipulator_ForbidSwap")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_GHOST_ITEM_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchGhostItemFix)
        .addClientMixins(
            "thaumcraft.common.items.wands.MixinItemWandCasting_DisableSpendingCheck",
            "thaumcraft.common.tiles.MixinTileMagicWorkbench_GhostItemFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_ALLOW_RECHARGE_CRAFTING(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchAllowRechargeCrafting)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileMagicWorkbenchCharger_AllowRechargeCrafting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ARCANE_WORKBENCH_MULTI_CONTAINER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchMultiContainer)
        .addCommonMixins("thaumcraft.common.container.MixinContainerArcaneWorkbench_MultiContainer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CACHE_ARCANE_WORKBENCH_RECIPE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchCache)
        .addCommonMixins(
            "thaumcraft.common.container.MixinContainerArcaneWorkbench_UseCache",
            "thaumcraft.common.lib.crafting.MixinThaumcraftCraftingManager_UseCache",
            "thaumcraft.common.tiles.MixinTileMagicWorkbench_CacheRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MUNDANE_CRAFT_FORGE_EVENT_BRIDGE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.arcaneWorkbenchForgeEventBridge)
        .addCommonMixins("thaumcraft.common.container.MixinSlotCraftingArcaneWorkbench_ForgeEventBridge")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    THAUMATORIUM_MULTI_CONTAINER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.thaumatoriumMultiContainer)
        .addCommonMixins("thaumcraft.common.container.MixinContainerThaumatorium_MultiContainer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NEGATIVE_BOSS_SPAWN_COUNT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.negativeBossSpawnCount)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileEldritchLock_NegativeBossSpawnCount")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WARP_FAKE_PLAYER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.warpFakePlayerCheck)
        .addCommonMixins("thaumcraft.common.MixinThaumcraft_FakePlayerWarp")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CRIMSON_RITES_FAKE_PLAYER(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.crimsonRitesFakePlayerCheck)
        .addCommonMixins("thaumcraft.common.items.MixinItemEldritchObject_FakePlayerFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_TRADE_BREAK_BLOCKS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.equalTradeBreaksBlocks)
        .addCommonMixins("thaumcraft.common.items.wands.foci.MixinItemFocusTrade_BreakBlocks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_RECHARGE_TIME(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.nodesRechargeInGameTime)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_RechargeTime")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_REMEMBER_DRAINED(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.nodesRememberBeingDrained)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_RememberUpdates")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_ICON_METADATA_PROTECTIONS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.itemMetadataSafetyCheck)
        .addClientMixins(
            "thaumcraft.common.items.Mixin_ItemIconFix",
            "thaumcraft.common.items.wands.MixinItemWandRod_MetadataSafety")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SILVERWOOD_LOG_NAME_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.silverwoodLogCorrectName)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockMagicalLogItem_NameFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    UPDATE_BIOME_COLOR(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.updateBiomeColorRendering)
        .addClientMixins("thaumcraft.common.lib.utils.MixinUtils_UpdateBiomeColor")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RUNED_STONE_CREATIVE_IMMUNITY(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.runedStoneIgnoreCreative)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileEldritchTrap_CreativeImmunity")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    WAND_FOCUS_LEVEL_PATCH(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.upgradedFocusVisCost)
        .addCommonMixins("thaumcraft.api.wands.MixinItemFocusBasic_WandUpgradeLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_NO_CREATIVE_DROPS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.jarNoCreativeDrops)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockJar_NoCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_NO_CREATIVE_DROPS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.bannerNoCreativeDrops)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockWoodenDevice_NoBannerCreativeDrops")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_PICK_BLOCK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.bannerPickBlock)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockWoodenDevice_BannerPickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    JAR_PICK_BLOCK(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.jarPickBlock)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockJar_PickBlock")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ITEM_COUNTING_FIX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.correctItemInsertion)
        .addCommonMixins("thaumcraft.common.lib.utils.MixinInventoryUtils_AmountCounting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ETHEREALBLOOM_SAVE_NBT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.etherealBloomSaveNBT)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileEtherealBloom_SaveNBT")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SILK_TOUCH_CRYSTALS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.silkTouchCrystalClusters)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockCrystal_SilkTouch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    LOOT_BLOCK_HITBOX(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.lootBlockHitbox)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockLoot_SetHitbox")
        .addClientMixins("thaumcraft.client.renderers.block.MixinBlockLootRenderer_ConserveBlockBounds")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FIX_LOCALIZATION_SIDES(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.fixClientSideLocalization)
        .addCommonMixins(
            "thaumcraft.common.blocks.MixinBlockMetalDevice_LocalizeCorrectly",
            "thaumcraft.common.blocks.MixinBlockMirrorItem_LocalizableText",
            "thaumcraft.common.blocks.MixinBlockStoneDevice_LocalizeCorrectly",
            "thaumcraft.common.blocks.MixinBlockWoodenDevice_LocalizableText",
            "thaumcraft.common.items.relics.MixinItemHandMirror_LocalizeCorrectly",
            "thaumcraft.common.items.MixinItemKey_LocalizeCorrectly",
            "thaumcraft.common.items.MixinItemResearchNotes_LocalizeCorrectly",
            "thaumcraft.common.entities.monster.boss.MixinEntityThaumcraftBosses_LocalizeCorrectly",
            "thaumcraft.common.lib.events.MixinEventHandlerEntity_LocalizeCorrectly",
            "thaumcraft.common.lib.network.playerdata.MixinPacketPlayerCompleteToServer_LocalizeCorrectly",
            "thaumcraft.common.lib.MixinWarpEvents_LocalizeCorrectly",
            "thaumcraft.common.tiles.MixinTileEldritchLock_LocalizeCorrectly")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXCAVATION_DETERMINISTIC_COST(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.excavationFocusDeterministicCost)
        .addCommonMixins("thaumcraft.common.items.wands.foci.MixinItemFocusExcavation_DeterministicCost")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    BANNER_PHIAL_CONSUMPTION(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.bugfixes.bannerReturnPhials.isEnabled() || SalisConfig.features.bannerFreePatterns.isEnabled() || SalisConfig.features.stopCreativeModeItemConsumption.isEnabled())
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockWoodenDevice_BannerPhialConsumption")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    KEY_EXTRA_SECURITY(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.extraSecureArcaneKeys)
        .addCommonMixins("thaumcraft.common.items.MixinItemKey_ExtraSecurityChecks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EARTH_SHOCK_REQUIRE_SOLID_GROUND(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.earthShockRequireSolidGround)
        .addCommonMixins("thaumcraft.common.entities.projectile.MixinEntityShockOrb_CheckSolidGround", "thaumcraft.common.blocks.MixinBlockAiry_EarthShockCheckSolidGround")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DEFAULT_WAND_COMPONENTS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.unknownWandComponentSupport)
        .addCommonMixins("thaumcraft.common.items.wands.MixinItemWandCasting_DefaultWandComponents")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CLAMP_WAND_OVERLAY_VIS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.clampWandOverlayVis)
        .addClientMixins("thaumcraft.client.lib.MixinClientTickEventsFML_VisOverflow")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXTEND_FOCUS_UPGRADE_PACKET(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.extendUpgradeFocusPacket)
        .addClientMixins("thaumcraft.client.gui.MixinGuiFocalManipulator_UseExtendedEnchantmentPacket")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PREVENT_WARP_SOUND_BLAST(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.muteExcessiveWarpSounds)
        .addClientMixins("thaumcraft.common.lib.network.playerdata.MixinPacketWarpMessage_MuteExcessiveSounds")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EARLY_TERMINATE_CRUCIBLE_CRAFT(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.earlyTerminateCrucibleCraft)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileCrucible_EarlyTerminateCraft")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PREVENT_INVALID_FOCI(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.preventInvalidFociOnWands)
        .addCommonMixins("thaumcraft.common.items.wands.MixinItemWandCasting_InvalidFoci")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    HIDDEN_RESEARCH_WORLD_RANDOM(new SalisBuilder()
        .applyIf(SalisConfig.thaum.hiddenResearchUseWorldRandom)
        .addCommonMixins("thaumcraft.common.lib.research.MixinResearchManager_RandomizeProperly")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    HIDDEN_RESEARCH_CHECK_INVENTORY(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.hiddenResearchCheckInventory)
        .addCommonMixins("thaumcraft.common.lib.research.MixinResearchManager_SkipResearchInInventory")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PREVENT_DEAD_ITEM_CRUCIBLE_DUPE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.crucibleDeadItemDupe)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileCrucible_NoDupeDeadItems")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PREVENT_GOLEM_DROP_DUPLICATION(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.preventGolemDropDuplication)
        .addCommonMixins("thaumcraft.common.entities.golems.MixinItemGolemBell_PreventDeadInteractions")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ELEMENTAL_PICK_SCAN_ZERO_ASPECTS(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.detectZeroAspectBlocks)
        .addClientMixins("thaumcraft.client.lib.MixinRenderEventHandler_DetectZeroAspectBlocks")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ELEMENTAL_PICK_SCAN_DETECT_LIT_REDSTONE_ORE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.detectLitRedstoneOre)
        .addClientMixins("thaumcraft.client.lib.MixinRenderEventHandler_DetectLitRedstoneOre")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    ELEMENTAL_PICK_SCAN_DETECT_LAPIS_ORE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.detectLapisOre)
        .addClientMixins("thaumcraft.client.lib.MixinRenderEventHandler_DetectLapisOre")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    AUTOMAGY_BOILER_FAKEPLAYER(new SalisBuilder()
        .applyIf(SalisConfig.addons.automagyBoilerFakePlayer)
        .addCommonMixins("automagy.blocks.MixinBlockBoiler_FakePlayer")
        .addRequiredMod(TargetedMod.AUTOMAGY)),
    RUNIC_MATRIX_OVERSTABLE_SHAKE(new SalisBuilder()
        .applyIf(SalisConfig.bugfixes.stableRunicMatrixAnimation)
        .addClientMixins("thaumcraft.client.renderers.tile.MixinTileRunicMatrixRenderer_StableAltar")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    // Features
    EXTENDED_BAUBLES_SUPPORT(new SalisBuilder()
        .applyIf(SalisConfig.features.useAllBaublesSlots)
        .addCommonMixins(
            "thaumcraft.common.lib.events.MixinEventHandlerRunic_ExtendedBaublesSupport",
            "thaumcraft.common.items.wands.MixinWandManager_ExtendedBaublesSupport",
            "thaumcraft.common.lib.MixinWarpEvents_BaubleSlots")
        .addClientMixins("thaumcraft.client.lib.MixinREHWandHandler_ExtendedBaublesSupport")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new SalisBuilder()
        .applyIf(SalisConfig.features.suppressWarpEventsInCreative)
        .addCommonMixins("thaumcraft.common.lib.events.MixinEventHandlerEntity_SuppressCreativeWarp")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconScrollwheelEnabled)
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconShowResearchId)
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchBrowser_ShowResearchID")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIGATION(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconRightClickClose)
        .addClientMixins(
            "thaumcraft.client.gui.MixinGuiResearchBrowser_RightClickClose",
            "thaumcraft.client.gui.MixinGuiResearchRecipe_RightClickNavigation")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new SalisBuilder()
        .applyIf(SalisConfig.features.nodeModifierWeights)
        .addCommonMixins("thaumcraft.common.lib.world.MixinThaumcraftWorldGenerator_NodeGenerationWeights")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_TYPE_WEIGHTS(new SalisBuilder()
        .applyIf(SalisConfig.features.nodeTypeWeights)
        .addCommonMixins("thaumcraft.common.lib.world.MixinThaumcraftWorldGenerator_NodeGenerationWeights")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new SalisBuilder()
        .applyIf(SalisConfig.features.stabilizerRewrite)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.wandPedestalUseCV.isEnabled() && !MixinModCompat.disableWandCV)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileWandPedestal_WandPedestalCv")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ITEM_ELDRITCH_OBJECT_STACK_SIZE(new SalisBuilder()
        .applyIf(SalisConfig.features.itemEldritchObjectStackSize)
        .addCommonMixins("thaumcraft.common.items.MixinItemEldritchObject_EldritchObjectStackSize")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_ITEM_CONSUMPTION(new SalisBuilder()
        .applyIf(SalisConfig.features.stopCreativeModeItemConsumption)
        .addCommonMixins(
            "thaumcraft.common.blocks.MixinBlockEldritch_CreativeMode",
            "thaumcraft.common.blocks.MixinBlockMetalDevice_CreativePreserveWater",
            "thaumcraft.common.items.MixinItemEssence_CreativeItemConsumption")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_MODE_VIS_CONSUMPTION(new SalisBuilder()
        .applyIf(SalisConfig.features.infiniteCreativeVis)
        .addCommonMixins("thaumcraft.common.items.wands.MixinItemWandCasting_CreativeVis")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MANA_POD_GROWTH_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.manaPodGrowthRate)
        .addCommonMixins("thaumcraft.common.blocks.MixinBlockManaPod_GrowthRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_TAB_COMPLETION(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumcraftCommandTabCompletion)
        .addCommonMixins("thaumcraft.common.lib.events.MixinCommandThaumcraft_TabCompletion")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMCRAFT_COMMAND_WARP_ARG_ALL(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumcraftCommandWarpArgAll)
        .addCommonMixins("thaumcraft.common.lib.events.MixinCommandThaumcraft_WarpArg")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_SCAN_CONTAINERS(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumometerScanContainers)
        .addCommonMixins(
            "thaumcraft.common.items.relics.MixinItemThaumometer_ScanContainers",
            "thaumcraft.common.lib.research.MixinScanManager_ScanContainers")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    CREATIVE_OP_THAUMONOMICON(new SalisBuilder()
        .applyIf(SalisConfig.features.creativeOpThaumonomicon)
        .addCommonMixins("thaumcraft.common.lib.research.MixinResearchManager_CreativeOPThaumonomicon")
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchBrowser_Creative_Scroll")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    CREATIVE_NO_XP_MANIPULATOR(new SalisBuilder()
        .applyIf(SalisConfig.features.creativeNoXPManipulator)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileFocalManipulator_NoXP")
        .addClientMixins("thaumcraft.client.gui.MixinGuiFocalManipulator_CreativeNoXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_STORE_XP(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.enableFocusDisenchanting.isEnabled() || SalisConfig.features.focalDisenchanterReturnXP.isEnabled())
        .addCommonMixins(
            "thaumcraft.common.tiles.MixinTileFocalManipulator_CanStoreXP",
            "thaumcraft.common.container.MixinContainerFocalManipulator_StoreXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCAL_MANIPULATOR_RETURN_XP(new SalisBuilder()
        .applyIf(SalisConfig.features.focalDisenchanterReturnXP)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileFocalManipulator_CancelReturnXP")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FOCUS_DISENCHANTING(new SalisBuilder()
        .applyIf(SalisConfig.features.enableFocusDisenchanting)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileFocalManipulator_FocusDisenchanting")
        .addClientMixins("thaumcraft.client.gui.MixinGuiFocalManipulator_FocusDisenchanting")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    LEVITATOR_SHIFT_FIX(new SalisBuilder()
        .applyIf(SalisConfig.features.levitatorShiftFix)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileLifter_LevitatorShiftFix")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PURE_NODE_BIOMECHANGE(new SalisBuilder()
        .applyIf(SalisConfig.features.pureNodeBiomeChange)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_PureNodeBiomeChange")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ELDRITCH_ALTAR_EVEN_SPREAD_MOBS(new SalisBuilder()
        .applyIf(SalisConfig.features.eldritchAltarSpawningMethod)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileEldritchAltar_SpawnMobs")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    TAINTED_ITEM_DECAY_CHANCE(new SalisBuilder()
        .applyIf(SalisConfig.features.taintedItemDecayChance)
        .addCommonMixins("thaumcraft.common.items.MixinItemResource_DecayChance")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    DISABLE_CREATIVE_TAINTED_ITEM_DECAY(new SalisBuilder()
        .applyIf(SalisConfig.features.disableCreativeTaintedItemDecay)
        .addCommonMixins("thaumcraft.common.items.MixinItemResource_DisableCreativeDecay")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NAMED_STAFFTERS(new SalisBuilder()
        .applyIf(SalisConfig.features.staffterNameTooltip)
        .addCommonMixins("thaumcraft.common.items.wands.MixinItemWandCasting_NamedStaffters")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    SINGLE_WAND_REPLACEMENT(new SalisBuilder()
        .setApplyIf(SalisConfig.features::singleWandReplacementEnabled)
        .addCommonMixins("thaumcraft.common.container.MixinContainerArcaneWorkbench_SingleWandReplacement")
        .addClientMixins("thaumcraft.client.gui.MixinGuiArcaneWorkbench_SingleWandReplacement")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    PRIMAL_CRUSHER_OREDICT_COMPAT(new SalisBuilder()
        .applyIf(SalisConfig.features.primalCrusherOredict)
        .addCommonMixins("thaumcraft.common.items.equipment.PrimalCrusher_StoneOredictCompat")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_FOCUS_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.equalTradeFocusHarvestLevel)
        .addCommonMixins("thaumcraft.common.items.wands.foci.MixinItemFocusTrade_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EXCAVATION_FOCUS_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.excavationFocusHarvestLevel)
        .addCommonMixins("thaumcraft.common.items.wands.foci.MixinItemFocusExcavation_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    EQUAL_TRADE_POTENCY_UPGRADE(new SalisBuilder()
        .setApplyIf(() -> SalisConfig.features.potencyModifiesHarvestLevel.isEnabled() && SalisConfig.features.equalTradeFocusHarvestLevel.isEnabled())
        .addCommonMixins("thaumcraft.common.items.wands.foci.MixinItemFocusTrade_AddPotency")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    PRIMAL_CRUSHER_HARVEST_LEVEL(new SalisBuilder()
        .applyIf(SalisConfig.features.crusherHarvestLevel)
        .addCommonMixins("thaumcraft.common.items.equipment.MixinItemPrimalCrusher_HarvestLevel")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    THAUMOMETER_CUSTOM_DURATION(new SalisBuilder()
        .applyIf(SalisConfig.features.thaumometerDuration)
        .addCommonMixins("thaumcraft.common.items.relics.MixinItemThaumometer_CustomDuration")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MISSING_RESEARCH_INFUSION(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchInfusion)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileInfusionMatrix_MissingResearch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_CRUCIBLE(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchCrucible)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileCrucible_MissingRecipe")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    MISSING_RESEARCH_WORKBENCH(new SalisBuilder()
        .applyIf(SalisConfig.features.notifyMissingResearchWorkbench)
        .addCommonMixins("thaumcraft.common.lib.crafting.MixinArcaneSceptreRecipe_MissingResearchWorkbench", "thaumcraft.common.lib.crafting.MixinArcaneWandRecipe_MissingResearchWorkbench")
        .addClientMixins("thaumcraft.client.gui.MixinGuiArcaneWorkbench_MissingResearch")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    RESEARCH_ITEM_EXTENDED(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("thaumcraft.api.research.ResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ITEM_EXTENDED_THAUMIC_TINKERER(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("thaumic.tinkerer.common.research.TTResearchItem_Extended")
        .addRequiredMod(TargetedMod.THAUMIC_TINKERER)),
    RESEARCH_ITEM_EXTENDED_AUTOMAGY(new SalisBuilder()
        .applyIf(SalisConfig.features.researchItemExtensions)
        .addCommonMixins("automagy.config.ModResearchItem_Extended")
        .addRequiredMod(TargetedMod.AUTOMAGY)),

    CRUCIBLE_HEAT_SOURCES(new SalisBuilder()
        .applyIf(SalisConfig.features.heatSourceOreDict)
        .addCommonMixins(
            "thaumcraft.common.tiles.MixinTileCrucible_HeatSources",
            "thaumcraft.common.tiles.MixinTileThaumatorium_HeatSources")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    VIS_RELAY_BOX_EXPANSION(new SalisBuilder()
        .applyIf(SalisConfig.features.visRelayBoxExpansion)
        .addCommonMixins(
            "thaumcraft.common.tiles.MixinTileVisRelay_ExpandBoundingBox",
            "thaumcraft.common.items.baubles.MixinItemAmuletVis_ExpandBoundingBox")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TICK_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletTickRate)
        .addCommonMixins("thaumcraft.common.items.baubles.MixinItemAmuletVis_TickRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_TRANSFER_RATE(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletTransferRate)
        .addCommonMixins("thaumcraft.common.items.baubles.MixinItemAmuletVis_TransferRate")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    VIS_AMULET_FULL_INVENTORY(new SalisBuilder()
        .applyIf(SalisConfig.features.visAmuletCheckInventory)
        .addCommonMixins("thaumcraft.common.items.baubles.MixinItemAmuletVis_InventoryCheck")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    MOB_VIS_WHITELIST(new SalisBuilder()
        .setApplyIf(() -> !SalisConfig.features.mobVisWhitelist.isEnabled() || SalisConfig.features.mobVisDropList.getNonEmpty().length != 0)
        .addCommonMixins("thaumcraft.common.lib.events.MixinEventHandlerEntity_MobVisWhitelist")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    DEADLY_GAZE_MOB_CHECK(new SalisBuilder()
        .applyIf(SalisConfig.features.deadlyGazeMobCheck)
        .addCommonMixins("thaumcraft.common.lib.MixinWarpEvents_DeadlyGaze")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    DUPLICATION_BUTTON(new SalisBuilder()
        .applyIf(SalisConfig.features.nomiconDuplicateResearch)
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchBrowser_DuplicateButton")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    FREE_DUPLICATES(new SalisBuilder()
        .applyIf(SalisConfig.features.researchDuplicationFree)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileResearchTable_FreeDuplicates")
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchTable_FreeDuplicates")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_UNKNOWN_ASPECT_HINT(new SalisBuilder()
        .applyIf(SalisConfig.features.researchTableAspectHints)
        .addClientMixins("thaumcraft.client.gui.MixinGuiResearchTable_UnknownAspectTooltip")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    FOCUS_POUCH_SLOT(new SalisBuilder()
        .applyIf(SalisConfig.modCompat.baublesExpanded.focusPouchSlot)
        .addCommonMixins("thaumcraft.common.items.wands.MixinItemFocusPouchBauble_ExpandedBaublesSlot")
        .addRequiredMod(TargetedMod.THAUMCRAFT)
        .addRequiredMod(TargetedMod.BAUBLES_EXPANDED)),

    // Tweaks

    POTION_ID_OVERRIDE(new SalisBuilder()
        .setApplyIf(SalisConfig.thaum::anyPotionIdOverrideActive)
        .addCommonMixins("thaumcraft.common.config.MixinConfig_PotionIds")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    ELEMENTAL_PICK_SCAN_OREDICT(new SalisBuilder()
        .applyIf(SalisConfig.thaum.elementalPickScanTags)
        .addClientMixins("thaumcraft.client.lib.MixinRenderEventHandler_ElementalPickOredict")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_DYNAMIC_REACH_DARK(new SalisBuilder()
        .applyIf(SalisConfig.thaum.sinisterDynamicReach)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_DynamicReach_Dark")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_DYNAMIC_REACH_HUNGRY(new SalisBuilder()
        .applyIf(SalisConfig.thaum.hungryDynamicReach)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_DynamicReach_Hungry")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_DYNAMIC_REACH_PURE(new SalisBuilder()
        .applyIf(SalisConfig.thaum.pureDynamicReach)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_DynamicReach_Pure")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_DYNAMIC_REACH_TAINTED(new SalisBuilder()
        .applyIf(SalisConfig.thaum.taintedDynamicReach)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_DynamicReach_Tainted")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    NODE_MODIFIER_SPEED_DARK(new SalisBuilder()
        .applyIf(SalisConfig.thaum.sinisterModifierSpeed)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_ModifierSpeed_Dark")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_MODIFIER_SPEED_HUNGRY(new SalisBuilder()
        .applyIf(SalisConfig.thaum.hungryModifierSpeed)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_ModifierSpeed_Hungry")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_MODIFIER_SPEED_PURE(new SalisBuilder()
        .applyIf(SalisConfig.thaum.pureModifierSpeed)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_ModifierSpeed_Pure")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),
    NODE_MODIFIER_SPEED_TAINTED(new SalisBuilder()
        .applyIf(SalisConfig.thaum.taintedModifierSpeed)
        .addCommonMixins("thaumcraft.common.tiles.MixinTileNode_ModifierSpeed_Tainted")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    FAKE_PLAYERS_DROP_LOOTBAGS(new SalisBuilder()
        .applyIf(SalisConfig.features.fakePlayersDropLootbags)
        .addCommonMixins("thaumcraft.common.lib.events.MixinEventHandlerEntity_LootBagFakePlayer")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    // Required
    ADD_VISCONTAINER_INTERFACE(new SalisBuilder()
        .setRequired()
        .addCommonMixins("thaumcraft.common.items.MixinAmuletWand_AddInterface")
        .addRequiredMod(TargetedMod.THAUMCRAFT)),

    // spotless:on

    // This entry can be used during development to ensure the string->class file check in SalisBuilder is working
    // correctly. It should stay commented out except during development, and should not be uncommented when merged into
    // main.
    // INTENTIONAL_FAILURE(new SalisBuilder()
    // .setRequired()
    // .addCommonMixins("Intentionally_missing_class_name")),
    ;

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

        public MixinBuilder setRequired() {
            return super.setApplyIf(() -> true);
        }

        // The below can be used during development to ensure that the strings passed to the builder correctly map to
        // class files. It should stay commented out except during development, and should not be uncommented when
        // merged into main. If you need to check that this is working correctly, you can un-comment the
        // `INTENTIONAL_FAILURE` enum value above.

        // @Override
        // public MixinBuilder addServerMixins(@Nonnull String... classes) {
        // assertClassesExist(classes);
        // return super.addServerMixins(classes);
        // }
        //
        // @Override
        // public MixinBuilder addClientMixins(@Nonnull String... classes) {
        // assertClassesExist(classes);
        // return super.addClientMixins(classes);
        // }
        //
        // @Override
        // public MixinBuilder addCommonMixins(@Nonnull String... classes) {
        // assertClassesExist(classes);
        // return super.addCommonMixins(classes);
        // }
        //
        // private static void assertClassesExist(String[] classes) {
        // for (var clazz : classes) {
        // final var classUrl = Mixins.class.getResource(
        // String.format("/dev/rndmorris/salisarcana/mixins/late/%s.class", clazz.replace('.', '/')));
        // if (classUrl == null) {
        // throw new RuntimeException(String.format("The Salis Arcana mixin `%s` was not found.", clazz));
        // }
        // }
        // }
    }
}
