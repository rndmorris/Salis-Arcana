package dev.rndmorris.salisarcana.config.group;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.BlockItemListSetting;
import dev.rndmorris.salisarcana.config.settings.CustomResearchSetting;
import dev.rndmorris.salisarcana.config.settings.EldritchAltarMobSpawnSetting;
import dev.rndmorris.salisarcana.config.settings.FloatSetting;
import dev.rndmorris.salisarcana.config.settings.IntArraySetting;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.config.settings.ReplaceWandComponentSettings;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.config.settings.StringArraySetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class ConfigFeatures extends ConfigGroup {

    public final ToggleSetting lessPickyPrimalCharmRecipe = new ToggleSetting(
        this,
        "friendlyPrimalCharm",
        "Make the primal charm's crafting recipe less picky about the order in which primal shards are placed in the top and bottom rows.")
            .setCategory("recipes");

    public final ToggleSetting rotatedFociRecipes = new ToggleSetting(
        this,
        "rotatedFoci",
        "Add rotated recipes for the fire, shock, frost, equal rade, excavation, and primal wand foci.")
            .setCategory("recipes");

    public final ToggleSetting rotatedThaumometerRecipe = new ToggleSetting(
        this,
        "rotatedThaumometer",
        "Add a rotated crafting recipe for the Thaumometer.").setCategory("recipes");

    public final CustomResearchSetting replaceWandCapsSettings = new ReplaceWandComponentSettings(
        this,
        ReplaceWandComponentSettings.Component.CAPS,
        new CustomResearchSetting.ResearchInfo("REPLACEWANDCAPS", "THAUMATURGY", 4, 2).setParents("CAP_gold")
            .setAutoUnlock()).setCategory("wand_component_swapping");

    public final ReplaceWandComponentSettings replaceWandCoreSettings = new ReplaceWandComponentSettings(
        this,
        ReplaceWandComponentSettings.Component.CORE,
        new CustomResearchSetting.ResearchInfo("REPLACEWANDCORE", "THAUMATURGY", -6, 2).setParents("ROD_greatwood")
            .setAutoUnlock()).setCategory("wand_component_swapping");

    public final ToggleSetting enforceWandCoreTypes = new ToggleSetting(this, "enforceWandCoreTypes", """
        If enabled, prevents swapping a wand core with a staff core or a staff core with a wand core.
        Disable to allow upgrading a wand to a staff and vice versa.""").setCategory("wand_component_swapping");

    public final ToggleSetting preserveWandVis = new ToggleSetting(
        this,
        "preserveWandVis",
        "If enabled, vis will be preserved when a wand, staff, or stave's components are replaced.")
            .setCategory("wand_component_swapping");

    public final ToggleSetting allowSingleWandReplacement = new ToggleSetting(
        this,
        "allowSingleWandReplacement",
        "If enabled, allows swapping a wand's components using vis from the wand being modified.")
            .setCategory("wand_component_swapping");

    public final ToggleSetting lookalikePlanks = new ToggleSetting(
        this,
        "enableLookalikePlanks",
        "Add look-a-like greatwood and silverwood planks that behave as normal planks, instead of the weirdness of TC4's planks.");

    public final IntArraySetting nodeModifierWeights = new IntArraySetting(
        this,
        "nodeModifierWeights",
        "Node Modifier Worldgen Weights (normal, bright, pale, fading)",
        // calculated based on TC4's default `specialNodeRarity` value
        new int[] { 972222, 9259, 9259, 9259, },
        0,
        1000000).setEnabled(false)
            .setLengthFixed(true);

    public final IntArraySetting nodeTypeWeights = new IntArraySetting(
        this,
        "nodeTypeWeights",
        "Node Type Worldgen Weights (normal, unstable, dark, pure, hungry)",
        // calculated based on TC4's default `specialNodeRarity` value
        new int[] { 944444, 16666, 16666, 16666, 5555, },
        0,
        1000000).setEnabled(false)
            .setLengthFixed(true);

    public final ToggleSetting suppressWarpEventsInCreative = new ToggleSetting(
        this,
        "suppressWarpEventsInCreative",
        "Prevent random warp events from firing for players in creative mode.");

    public final ToggleSetting useAllBaublesSlots = new ToggleSetting(
        this,
        "useAllBaublesSlots",
        "Enables support for mods that increase the number of baubles slots.");

    public final ToggleSetting stopCreativeModeItemConsumption = new ToggleSetting(
        this,
        "stopCreativeModeItemConsumption",
        "Prevent eldritch eyes and phials of essentia from being consumed when used in creative mode.");

    public final IntSetting manaPodGrowthRate = new IntSetting(
        this,
        "manaBeanGrowthChance",
        "The chance for a mana bean to grow when a mana pod is updated. Lower values are more likely to grow, with 0 growing every random tick.",
        30).setMinValue(0)
            .setMaxValue(100);

    public final ToggleSetting infiniteCreativeVis = new ToggleSetting(
        this,
        "infiniteCreativeVis",
        "Allow wands to have infinite vis in creative mode.");

    public final ToggleSetting thaumicInventoryScanning = new ToggleSetting(
        this,
        "thaumicInventoryScanning",
        "Enable the ability to scan items in inventories using the Thaumometer. Replaces the Thaumic Inventory Scanning mod.");

    public final ToggleSetting thaumcraftCommandTabCompletion = new ToggleSetting(
        this,
        "thaumcraftCommandTabCompletion",
        "Enable tab completion for Thaumcraft commands.");

    public final ToggleSetting thaumcraftCommandWarpArgAll = new ToggleSetting(
        this,
        "thaumcraftCommandWarpArgAll",
        "Allow the use of `ALL` as an argument for the warp command.");

    public final ToggleSetting creativeOpThaumonomicon = new ToggleSetting(
        this,
        "creativeOpThaumonomicon",
        "While in creative mode, ctrl + left click on a research in the Thaumonomicon to complete it.");

    public final ToggleSetting creativeNoXPManipulator = new ToggleSetting(
        this,
        "creativeNoXPManipulator",
        "Allow Creative players to use the Focal Manipulator without the necessary XP.");

    public final ToggleSetting focalDisenchanterReturnXP = new ToggleSetting(
        this,
        "focalDisenchanterReturnXP",
        "If an upgrade fails to complete or is cancelled, the XP spent will get returned to the player.");

    public final ToggleSetting enableFocusDisenchanting = new ToggleSetting(
        this,
        "enableFocusDisenchanting",
        "Allow players to use the Focal Manipulator to remove focus enchantments and refund XP.")
            .setCategory("focus_disenchanting");

    public final IntSetting focusDisenchantingRefundPercentage = new IntSetting(
        enableFocusDisenchanting,
        "focusDisenchantingRefundPercentage",
        "Percentage of XP points refunded upon removing an enchantment from a focus, calculated as levels from 0 XP.",
        75).setMinValue(0)
            .setMaxValue(100)
            .setCategory("focus_disenchanting");

    public final CustomResearchSetting focusDisenchantingResearch = new CustomResearchSetting(
        enableFocusDisenchanting,
        "focusDisenchanting",
        "Research to unlock Focus Disenchanting in the Focal Manipulator.",
        new CustomResearchSetting.ResearchInfo("FOCUS_DISENCHANTING", "THAUMATURGY", -2, -8).setDifficulty(2)
            .setParents("FOCALMANIPULATION")
            .setPurchasable(true)
            .setAspects("auram:4", "praecantatio:6", "vacuos:8", "perditio:4")).setCategory("focus_disenchanting");

    public final Setting nomiconScrollwheelEnabled = new ToggleSetting(
        this,
        "Enable Scrollwheel",
        "While viewing the Thaumonomicon, enables ctrl + scroll to quick switch tabs");

    public final Setting nomiconInvertedScrolling = new ToggleSetting(
        this,
        "Inverse Scrolling",
        "While viewing the Thaumonomicon, inverts the scrolling for tab switching").setEnabled(false);

    public final Setting nomiconRightClickClose = new ToggleSetting(
        this,
        "Right-Click Navigation",
        "While viewing the Thaumonomicon, right clicking in a research will take you back to the previous research, or back to the Thaumonomicon.");

    public final ToggleSetting nomiconSavePage = new ToggleSetting(
        nomiconRightClickClose,
        "Save Thaumonomicon Page",
        "When closing the Thaumonomicon, it will remember the page you are on when it is reopened. Requires Right-Click Navigation to be enabled.")
            .setEnabled(false);

    public final Setting nomiconShowResearchId = new ToggleSetting(
        this,
        "Show Research Key",
        "While viewing the Thaumonomicon, allows you to view the internal name of a research while hovering over it and holding control");

    public final ToggleSetting nomiconDuplicateResearch = new ToggleSetting(
        this,
        "nomiconDuplicateResearch",
        "Allow players with \"Research Duplication\" researched to create duplicates out of the Thaumonomicon.");

    public final ToggleSetting researchDuplicationFree = new ToggleSetting(
        this,
        "researchDuplicationFree",
        "Research duplication (in the Research Table and the Thaumonomicon if nomiconDuplicateResearch is enabled) does not cost any research aspects.")
            .setEnabled(false);

    public final ToggleSetting stabilizerRewrite = new ToggleSetting(
        this,
        "useStabilizerRewrite",
        "Rewrites the Runic Matrix's surroundings-check logic to be more flexible when checking for pedestals and stabilizers.")
            .setCategory("infusion")
            .setEnabled(false);

    public final IntSetting stabilizerStrength = new IntSetting(
        stabilizerRewrite,
        "stabilizerStrength",
        """
            Requires useStabilizerRewrite=true.
            The amount (in one-hundredths of a point) of symmetry each stabilizer block adds to an infusion altar.
            If a stabilizer doesn't have a symmetrical opposite, an equivalent amount of symmetry will be subtracted instead.
            """,
        10).setMinValue(-10000)
            .setMaxValue(10000)
            .setCategory("infusion");

    public final BlockItemListSetting<Integer> stabilizerAdditions = new BlockItemListSetting<Integer>(
        stabilizerRewrite,
        "stabilizerAdditions",
        """
            Requires useStabilizerRewrite=true.
            Blocks specified here will be factored into an infusion altar's symmetry, even if they normally would not.
            FORMAT: `modId:blockId` or `modId:blockId:metadata` or `modId:blockId:metadata:strength`.
              Metadata:
                * Defaults to 0 if not set.
                * If set to * or 32767, all metadata variants of the block will be included.
              Strength:
                * Defaults to `stabilizerStrength` if not set.
                * Range: -10000 ~ 10000.
            """).setListType(BlockItemListSetting.ListType.BLOCKS)
            .withAdditionalData((strSlice) -> {
                if (strSlice.length < 4) {
                    return null;
                }
                return IntegerHelper.tryParse(strSlice[3]);
            })
            .setCategory("infusion");

    public final BlockItemListSetting<Object> stabilizerExclusions = new BlockItemListSetting<>(
        stabilizerRewrite,
        "stabilizerExclusions",
        """
            Requires `useStabilizerRewrite=true`.
            Blocks specified here will NOT be factored into an infusion altar's symmetry even if they normally would.
            FORMAT: `modId:blockId` or `modId:blockId:metadata`.
              Metadata:
                * Defaults to 0 if not set.
                * If set to * or 32767, all metadata variants of the block will be included.
            """).setListType(BlockItemListSetting.ListType.BLOCKS)
            .setCategory("infusion");

    public final IntSetting itemEldritchObjectStackSize = new IntSetting(
        this,
        "eldritchObjectStackSize",
        "The maximum stack size for Eldritch Objects (Primordial Pearl, Eldritch Eye, Crimson Rites, Eldritch Obelisk Placer, Runed Tablet).",
        16).setMinValue(1)
            .setMaxValue(64);

    public final ToggleSetting disableCreativeTaintedItemDecay = new ToggleSetting(
        this,
        "disableCreativeTaintedItemDecay",
        "Prevent tainted goo and taint tendrils from decaying for players in creative mode.");

    public final IntSetting taintedItemDecayChance = new IntSetting(
        this,
        "taintedItemDecayChance",
        "The probability each tick that tainted goo and taint tendrils will decay. Lower numbers are more probable, higher numbers are less probable. Set to -1 to disable decay entirely.",
        4321).setMinValue(-1);

    public final EldritchAltarMobSpawnSetting eldritchAltarSpawningMethod = new EldritchAltarMobSpawnSetting(
        this,
        "eldritchAltarSpawningMethod",
        "Override how eldritch altars pick where to try spawning crimson knights and eldritch guardians.");

    public final ToggleSetting wandPedestalUseCV = new ToggleSetting(
        this,
        "Wand Pedestal CV Support",
        "Allows wand pedestals to draw from centivis instead of just regular nodes");

    public final ToggleSetting thaumometerScanContainers = new ToggleSetting(
        this,
        "thaumometerScanContainers",
        "Allow the thaumometer to scan the contents of inventories when right-clicking on them.")
            .setCategory("thaumometer_container_scan");

    public final CustomResearchSetting thaumometerScanContainersResearch = new CustomResearchSetting(
        thaumometerScanContainers,
        "thaumometerScanContainers",
        "Enable the thaumometer to scan the contents of inventories when right-clicking on them.",
        new CustomResearchSetting.ResearchInfo("CHESTSCAN", "BASICS", 8, 3).setDifficulty(3)
            .setParents("DECONSTRUCTOR")
            .setAspects("ordo:10", "perditio:10", "permutatio:10")).setCategory("thaumometer_container_scan");

    public final ToggleSetting levitatorShiftFix = new ToggleSetting(
        this,
        "levitatorShiftFix",
        "Fixes some general jankiness with levitators, however, non-player entities will no longer be able to be lowered.");

    public final ToggleSetting pureNodeBiomeChange = new ToggleSetting(
        this,
        "pureNodeAlwaysMagicalForest",
        "By default, pure nodes only change the biome around them if they are either in tainted lands or inside of a silverwood tree. This setting allows pure nodes to change the biome around them regardless of their location.");

    public final ToggleSetting rottenFleshRecipe = new ToggleSetting(
        this,
        "rottenFleshRecipe",
        "Add a crafting recipe to convert flesh blocks back into rotten flesh.").setCategory("recipes");

    public final ToggleSetting crystalClusterUncrafting = new ToggleSetting(
        this,
        "crystalClusterUncrafting",
        "Add crafting recipes to convert crystal cluster blocks back into primal shards. Does not work for mixed crystal clusters.")
            .setCategory("recipes");

    public final ToggleSetting staffterNameTooltip = new ToggleSetting(
        this,
        "staffterNameTooltip",
        "Causes staffters to use their own translation string rather than being called \"Staff\" in the tooltip.");

    public final ToggleSetting primalCrusherOredict = new ToggleSetting(
        this,
        "primalCrusherMinesOredictionaryStone",
        "Allows the primal crusher to 3x3 mine blocks registered as stone, cobblestone, or stoneBricks in the ore dictionary.");

    public final IntSetting thaumometerDuration = new IntSetting(
        this,
        "thaumometerDuration",
        "The duration in ticks that the thaumometer takes to scan an object.",
        20).setMinValue(1);

    public final ToggleSetting researchItemExtensions = new ToggleSetting(
        this,
        "researchItemExtensions",
        "Adds additional functionality to internal research data. Used for compatibility with other mods (e.g. Automagy, Thaumic Tinkerer).");

    public final ToggleSetting heatSourceOreDict = new ToggleSetting(
        this,
        "heatSourceOreDict",
        "Enable ore dictionary support for crucible and thaumatorium heat sources.");

    public final ToggleSetting notifyMissingResearchWorkbench = new ToggleSetting(
        this,
        "notifyMissingResearchWorkbench",
        "Displays a \"missing research\" message in the Arcane Workbench GUI when recipe fails for lack of research.");

    public final ToggleSetting notifyMissingResearchInfusion = new ToggleSetting(
        this,
        "notifyMissingResearchInfusion",
        "Displays a \"missing research\" message to the player when an infusion recipe fails for lack of research.");

    public final ToggleSetting notifyMissingResearchCrucible = new ToggleSetting(
        this,
        "notifyMissingResearchCrucible",
        "Displays a \"missing research\" message to the player when a crucible recipe fails for lack of research.");

    public final IntSetting thaumiumHarvestLevel = new IntSetting(
        this,
        "thaumiumHarvestLevel",
        "Override the harvest level of thaumium tools.",
        3).setMinValue(0)
            .setCategory("harvestLevels");

    public final IntSetting elementalHarvestLevel = new IntSetting(
        this,
        "elementalHarvestLevel",
        "Override the harvest level of elemental tools.",
        3).setMinValue(0)
            .setCategory("harvestLevels");

    public final IntSetting voidHarvestLevel = new IntSetting(
        this,
        "voidHarvestLevel",
        "Override the harvest level of void metal tools.",
        4).setMinValue(0)
            .setCategory("harvestLevels");

    public final IntSetting crusherHarvestLevel = new IntSetting(
        this,
        "crusherHarvestLevel",
        "Override the harvest level of the primal crusher.",
        5).setMinValue(0)
            .setCategory("harvestLevels");

    public final IntSetting excavationFocusHarvestLevel = new IntSetting(
        this,
        "excavationFocusHarvestLevel",
        "Override the harvest level of the excavation focus. -1 ignores harvest levels (vanilla Thaumcraft behavior).",
        -1).setMinValue(-1)
            .setCategory("harvestLevels");

    public final IntSetting equalTradeFocusHarvestLevel = new IntSetting(
        this,
        "equalTradeFocusHarvestLevel",
        "Override the harvest level of the equal trade focus. -1 ignores harvest levels (vanilla Thaumcraft behavior).",
        -1).setMinValue(-1)
            .setCategory("harvestLevels");

    public final ToggleSetting potencyModifiesHarvestLevel = new ToggleSetting(
        this,
        "potencyModifiesHarvestLevel",
        "If enabled, the potency level of an equal trade or excavation focus will modify its harvest level by one level per level of potency.")
            .setCategory("harvestLevels");

    public final FloatSetting visRelayBoxExpansion = new FloatSetting(
        this,
        "visRelayBoxExpansion",
        "The amount to expand the bounding box of vis relays. This is used to increase the range at which amulets are charged.",
        5.0F).setMinValue(0.0F);

    public final IntSetting visAmuletTickRate = new IntSetting(
        this,
        "visAmuletRechargeSpeed",
        "The rate in ticks at which vis amulets recharge themselves and other items.",
        5).setMinValue(1);

    public final IntSetting visAmuletTransferRate = new IntSetting(
        this,
        "visAmuletTransferRate",
        "The amount of cv which an amulet can receive or move to other items in one transfer.",
        5).setMinValue(1);

    public final ToggleSetting visAmuletCheckInventory = new ToggleSetting(
        this,
        "visAmuletCheckInventory",
        "If enabled, amulets will check and recharge wands in the entire inventory instead of just the player's hand.");

    public final StringArraySetting mobVisDropList = new StringArraySetting(
        this,
        "mobVisDropBlacklist",
        "List of entities that can not generate vis orbs when killed.",
        new String[0]);

    public final ToggleSetting mobVisWhitelist = new ToggleSetting(
        this,
        "mobVisDropWhitelist",
        "If enabled, the blacklist will be treated as a whitelist instead instead.").setEnabled(false);

    public final ToggleSetting deadlyGazeMobCheck = new ToggleSetting(
        this,
        "deadlyGazeMobCheck",
        "If enabled, the Deadly Gaze effect will only affect mobs that are not invulnerable or immune to wither");

    public final ToggleSetting addEmptyPhialJarRecipes = new ToggleSetting(
        this,
        "addEmptyPhialJarRecipes",
        "Adds crafting recipes to empty a phial or jar of essentia.");

    public final ToggleSetting bannerFreePatterns = new ToggleSetting(
        this,
        "bannerFreePatterns",
        "Applying patterns to banners not consume the phial or the essentia. Overrides bannerReturnPhials in the bugfixes module.")
            .setEnabled(false);

    public final ToggleSetting wandPartStatsTooltip = new ToggleSetting(
        this,
        "wandPartStatsTooltip",
        "Wand caps & wand rods will show information about their vis capacity & discount in their tooltips.");

    public final ToggleSetting researchTableAspectHints = new ToggleSetting(
        this,
        "researchTableAspectHints",
        "Hovering over an unknown aspect inside the Research Table will show a tooltip with a hint about where you can find it.");

    public final ToggleSetting fakePlayersDropLootbags = new ToggleSetting(
        this,
        "fakePlayersDropLootbags",
        "Allows kills from fake players to drop loot bags from champion mobs.").setEnabled(true);

    public boolean singleWandReplacementEnabled() {
        return (this.replaceWandCapsSettings.isEnabled() || this.replaceWandCoreSettings.isEnabled())
            && SalisConfig.bugfixes.arcaneWorkbenchGhostItemFix.isEnabled()
            && this.allowSingleWandReplacement.isEnabled();
    }

    @Nonnull
    @Override
    public String getGroupName() {
        return "enhancements";
    }

    @Nonnull
    @Override
    public String getGroupComment() {
        return "Features and enhancements for Thaumcraft";
    }
}
