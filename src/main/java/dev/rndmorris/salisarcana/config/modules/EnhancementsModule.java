package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.BlockItemListSetting;
import dev.rndmorris.salisarcana.config.settings.CustomResearchSetting;
import dev.rndmorris.salisarcana.config.settings.EldritchAltarMobSpawnSetting;
import dev.rndmorris.salisarcana.config.settings.IntArraySetting;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.config.settings.ReplaceWandComponentSettings;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class EnhancementsModule extends BaseConfigModule {

    public final ToggleSetting lessPickyPrimalCharmRecipe;
    public final ToggleSetting rotatedFociRecipes;
    public final ToggleSetting rotatedThaumometerRecipe;

    public final CustomResearchSetting replaceWandCapsSettings;
    public final ReplaceWandComponentSettings replaceWandCoreSettings;
    public final ToggleSetting enforceWandCoreTypes;
    public final ToggleSetting preserveWandVis;
    public final ToggleSetting allowSingleWandReplacement;

    public final ToggleSetting lookalikePlanks;
    public final IntArraySetting nodeModifierWeights;
    public final IntArraySetting nodeTypeWeights;
    public final ToggleSetting suppressWarpEventsInCreative;
    public final ToggleSetting useAllBaublesSlots;
    public final ToggleSetting stopCreativeModeItemConsumption;
    public final IntSetting manaPodGrowthRate;
    public final ToggleSetting infiniteCreativeVis;
    public final ToggleSetting thaumicInventoryScanning;
    public final ToggleSetting thaumcraftCommandTabCompletion;
    public final ToggleSetting thaumcraftCommandWarpArgAll;
    public final ToggleSetting creativeOpThaumonomicon;

    public final Setting nomiconScrollwheelEnabled;
    public final Setting nomiconInvertedScrolling;
    public final Setting nomiconRightClickClose;
    public final Setting nomiconShowResearchId;

    public final ToggleSetting stabilizerRewrite;
    public final BlockItemListSetting<Integer> stabilizerAdditions;
    public final BlockItemListSetting<Object> stabilizerExclusions;
    public final IntSetting stabilizerStrength;

    public final IntSetting itemEldritchObjectStackSize;
    public final ToggleSetting disableCreativeTaintedItemDecay;
    public final IntSetting taintedItemDecayChance;

    public final EldritchAltarMobSpawnSetting eldritchAltarSpawningMethod;

    public final ToggleSetting wandPedestalUseCV;
    public final ToggleSetting thaumometerScanContainers;
    public final CustomResearchSetting thaumometerScanContainersResearch;
    public final ToggleSetting levitatorShiftFix;
    public final ToggleSetting pureNodeBiomeChange;
    public final ToggleSetting rottenFleshRecipe;
    public final ToggleSetting crystalClusterUncrafting;

    public final ToggleSetting staffterNameTooltip;
    public final ToggleSetting primalCrusherOredict;

    public final IntSetting thaumometerDuration;
    public final ToggleSetting researchItemExtensions;

    public EnhancementsModule() {
        // spotless:off
        addSettings(
            lessPickyPrimalCharmRecipe = new ToggleSetting(
                this,
                "friendlyPrimalCharm",
                "Make the primal charm's crafting recipe less picky about the order in which primal shards are placed in the top and bottom rows.")
                    .setCategory("recipes"),
            rotatedFociRecipes = new ToggleSetting(
                this,
                "rotatedFoci",
                "Add rotated recipes for the fire, shock, frost, equal rade, excavation, and primal wand foci.")
                    .setCategory("recipes"),
            rotatedThaumometerRecipe = new ToggleSetting(
                this,
                "rotatedThaumometer",
                "Add a rotated crafting recipe for the Thaumometer.").setCategory("recipes"),
            lookalikePlanks = new ToggleSetting(
                this,
                "enableLookalikePlanks",
                "Add look-a-like greatwood and silverwood planks that behave as normal planks, instead of the weirdness of TC4's planks."),
            staffterNameTooltip = new ToggleSetting(
                this,
                "staffterNameTooltip",
                "Causes staffters to use their own translation string rather than being called \"Staff\" in the tooltip."),
            nodeModifierWeights = new IntArraySetting(
                this,
                "nodeModifierWeights",
                "Node Modifier Worldgen Weights (normal, bright, pale, fading)",
                // calculated based on TC4's default `specialNodeRarity` value
                new int[] { 972222, 9259, 9259, 9259, },
                0,
                1000000).setEnabled(false),
            nodeTypeWeights = new IntArraySetting(
                this,
                "nodeTypeWeights",
                "Node Type Worldgen Weights (normal, unstable, dark, pure, hungry)",
                // calculated based on TC4's default `specialNodeRarity` value
                new int[] { 944444, 16666, 16666, 16666, 5555, },
                0,
                1000000).setEnabled(false),
            suppressWarpEventsInCreative = new ToggleSetting(
                this,
                "suppressWarpEventsInCreative",
                "Prevent random warp events from firing for players in creative mode."),
            useAllBaublesSlots = new ToggleSetting(
                this,
                "useAllBaublesSlots",
                "Enables support for mods that increase the number of baubles slots."),
            nomiconInvertedScrolling = new ToggleSetting(
                this,
                "Inverse Scrolling",
                "While viewing the Thaumonomicon, inverts the scrolling for tab switching").setEnabled(false),
            nomiconRightClickClose = new ToggleSetting(
                this,
                "Right-Click Navigation",
                "While viewing the Thaumonomicon, right clicking in a research will take you back to the previous research, or back to the Thaumonomicon."),
            nomiconScrollwheelEnabled = new ToggleSetting(
                this,
                "Enable Scrollwheel",
                "While viewing the Thaumonomicon, enables ctrl + scroll to quick switch tabs"),
            nomiconShowResearchId = new ToggleSetting(
                this,
                "Show Research Key",
                "While viewing the Thaumonomicon, allows you to view the internal name of a research while hovering over it and holding control"),
            wandPedestalUseCV = new ToggleSetting(
                this,
                "Wand Pedestal CV Support",
                "Allows wand pedestals to draw from centivis instead of just regular nodes"),
            stabilizerRewrite = new ToggleSetting(
                this,
                "useStabilizerRewrite",
                "Rewrites the Runic Matrix's surroundings-check logic to be more flexible when checking for pedestals and stabilizers.")
                    .setCategory("infusion")
                    .setEnabled(false),
            stabilizerStrength = new IntSetting(
                stabilizerRewrite,
                "stabilizerStrength",
                String.join(
                    "\n",
                    "Requires useStabilizerRewrite=true.",
                    "The amount (in one-hundredths of a point) of symmetry each stabilizer block adds to an infusion altar.",
                    "If a stabilizer doesn't have a symmetrical opposite, an equivalent amount of symmetry will be subtracted instead.",
                    ""),
                10).setMinValue(-10000)
                    .setMaxValue(10000)
                    .setCategory("infusion"),
            itemEldritchObjectStackSize = new IntSetting(
                this,
                "eldritchObjectStackSize",
                "The maximum stack size for Eldritch Objects (Primordial Pearl, Eldritch Eye, Crimson Rites, Eldritch Obelisk Placer, Runed Tablet).",
                16).setMinValue(1)
                    .setMaxValue(64),
            stopCreativeModeItemConsumption = new ToggleSetting(
                this,
                "stopCreativeModeItemConsumption",
                "Prevent eldritch eyes and phials of essentia from being consumed when used in creative mode."),
            infiniteCreativeVis = new ToggleSetting(
                this,
                "infiniteCreativeVis",
                "Allow wands to have infinite vis in creative mode."),
            manaPodGrowthRate = new IntSetting(
                this,
                "manaBeanGrowthChance",
                "The chance for a mana bean to grow when a mana pod is updated. Lower values are more likely to grow, with 0 growing every random tick.",
                30).setMinValue(0)
                    .setMaxValue(100),
            thaumicInventoryScanning = new ToggleSetting(
                this,
                "thaumicInventoryScanning",
                "Enable the ability to scan items in inventories using the Thaumometer. Replaces the Thaumic Inventory Scanning mod."),
            thaumometerScanContainers = new ToggleSetting(
                this,
                "thaumometerScanContainers",
                "Allow the thaumometer to scan the contents of inventories when right-clicking on them.").setCategory("thaumometer_container_scan"),
            thaumcraftCommandTabCompletion = new ToggleSetting(
                this,
                "thaumcraftCommandTabCompletion",
                "Enable tab completion for Thaumcraft commands."),
            thaumcraftCommandWarpArgAll = new ToggleSetting(
                this,
                "thaumcraftCommandWarpArgAll",
                "Allow the use of `ALL` as an argument for the warp command."),
            creativeOpThaumonomicon = new ToggleSetting(
                this,
                "creativeOpThaumonomicon",

                "While in creative mode, ctrl + left click on a research in the Thaumonomicon to complete it."),
            levitatorShiftFix = new ToggleSetting(
                this,
                "levitatorShiftFix",
                "Fixes some general jankiness with levitators, however, non-player entities will no longer be able to be lowered."),
            pureNodeBiomeChange = new ToggleSetting(
                this,
                "pureNodeAlwaysMagicalForest",
                "By default, pure nodes only change the biome around them if they are either in tainted lands or inside of a silverwood tree. This setting allows pure nodes to change the biome around them regardless of their location."),
            eldritchAltarSpawningMethod = new EldritchAltarMobSpawnSetting(
                this,
                "eldritchAltarSpawningMethod",
                "Override how eldritch altars pick where to try spawning crimson knights and eldritch guardians."),
            rottenFleshRecipe = new ToggleSetting(
                this,
                "rottenFleshRecipe",
                "Add a crafting recipe to convert flesh blocks back into rotten flesh.").setCategory("recipes"),
            crystalClusterUncrafting = new ToggleSetting(
                this,
                "crystalClusterUncrafting",
                "Add crafting recipes to convert crystal cluster blocks back into primal shards. Does not work for mixed crystal clusters.").setCategory("recipes"),
            primalCrusherOredict = new ToggleSetting(
                this,
                "primalCrusherMinesOredictionaryStone",
                "Allows the primal crusher to 3x3 mine blocks registered as stone, cobblestone, or stoneBricks in the ore dictionary."),
            thaumometerDuration = new IntSetting(
                this,
                "thaumometerDuration",
                "The duration in ticks that the thaumometer takes to scan an object.",
                20).setMinValue(1),
            researchItemExtensions = new ToggleSetting(
                this,
                "researchItemExtensions",
                "Adds additional functionality to internal research data. Used for compatibility with other mods (e.g. Automagy, Thaumic Tinkerer).")
        );

        // spotless:on
        addSettings(
            stabilizerAdditions = new BlockItemListSetting<Integer>(
                stabilizerRewrite,
                "stabilizerAdditions",
                String.join(
                    "\n",
                    "Requires useStabilizerRewrite=true.",
                    "Blocks specified here will be factored into an infusion altar's symmetry, even if they normally would not.",
                    "FORMAT: `modId:blockId` or `modId:blockId:metadata` or `modId:blockId:metadata:strength`.",
                    "  Metadata:",
                    "    * Defaults to 0 if not set.",
                    "    * If set to * or 32767, all metadata variants of the block will be included.",
                    "  Strength:",
                    "    * Defaults to `stabilizerStrength` if not set.",
                    "    * Range: " + stabilizerStrength.getMinValue() + " ~ " + stabilizerStrength.getMaxValue() + ".",
                    "")).setListType(BlockItemListSetting.ListType.BLOCKS)
                        .withAdditionalData((strSlice) -> {
                            if (strSlice.length < 4) {
                                return null;
                            }
                            return IntegerHelper.tryParse(strSlice[3]);
                        })
                        .setCategory("infusion"));
        addSettings(
            stabilizerExclusions = new BlockItemListSetting<>(
                stabilizerRewrite,
                "stabilizerExclusions",
                String.join(
                    "\n",
                    "Requires useStabilizerRewrite=true.",
                    "Blocks specified here will NOT be factored into an infusion altar's symmetry even if they normally would.",
                    "FORMAT: `modId:blockId` or `modId:blockId:metadata`.",
                    "  Metadata:",
                    "    * Defaults to 0 if not set.",
                    "    * If set to * or 32767, all metadata variants of the block will be included.",
                    "")).setListType(BlockItemListSetting.ListType.BLOCKS)
                        .setCategory("infusion"));

        final var wandCategory = "wand_component_swapping";
        addSettings(
            replaceWandCapsSettings = new ReplaceWandComponentSettings(
                this,
                ReplaceWandComponentSettings.Component.CAPS,
                new CustomResearchSetting.ResearchInfo("REPLACEWANDCAPS", "THAUMATURGY", 4, 2).setParents("CAP_gold")
                    .setAutoUnlock()).setCategory(wandCategory),
            replaceWandCoreSettings = new ReplaceWandComponentSettings(
                this,
                ReplaceWandComponentSettings.Component.CORE,
                new CustomResearchSetting.ResearchInfo("REPLACEWANDCORE", "THAUMATURGY", -6, 2)
                    .setParents("ROD_greatwood")
                    .setAutoUnlock()).setCategory(wandCategory),
            enforceWandCoreTypes = new ToggleSetting(
                this,
                "enforceWandCoreTypes",
                "If enabled, prevents swapping a wand core with a staff core or a staff core with a wand core.\nDisable to allow upgrading a wand to a staff and vice versa.")
                    .setCategory(wandCategory),
            preserveWandVis = new ToggleSetting(
                this,
                "preserveWandVis",
                "If enabled, vis will be preserved when a wand, staff, or stave's components are replaced.")
                    .setCategory(wandCategory),
            allowSingleWandReplacement = new ToggleSetting(
                this,
                "allowSingleWandReplacement",
                "If enabled, allows swapping a wand's components using vis from the wand being modified.")
                    .setCategory(wandCategory),
            disableCreativeTaintedItemDecay = new ToggleSetting(
                this,
                "disableCreativeTaintedItemDecay",
                "Prevent tainted goo and taint tendrils from decaying for players in creative mode."),
            taintedItemDecayChance = new IntSetting(
                this,
                "taintedItemDecayChance",
                "The probability each tick that tainted goo and taint tendrils will decay. Lower numbers are more probable, higher numbers are less probable. Set to -1 to disable decay entirely.",
                4321).setMinValue(-1));

        addSettings(
            thaumometerScanContainersResearch = new CustomResearchSetting(
                thaumometerScanContainers,
                "thaumometerScanContainers",
                "Enable the thaumometer to scan the contents of inventories when right-clicking on them.",
                new CustomResearchSetting.ResearchInfo("CHESTSCAN", "BASICS", 8, 3).setDifficulty(3)
                    .setParents("DECONSTRUCTOR")
                    .setAspects("ordo:10", "perditio:10", "permutatio:10")).setCategory("thaumometer_container_scan"));

    }

    public boolean singleWandReplacementEnabled() {
        return (this.replaceWandCapsSettings.isEnabled() || this.replaceWandCoreSettings.isEnabled())
            && ConfigModuleRoot.bugfixes.arcaneWorkbenchGhostItemFix.isEnabled()
            && this.allowSingleWandReplacement.isEnabled();
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "enhancements";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Tweaks and adjustments to enhance Thaumcraft";
    }
}
