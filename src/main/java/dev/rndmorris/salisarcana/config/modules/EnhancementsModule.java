package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.BlockItemListSetting;
import dev.rndmorris.salisarcana.config.settings.IntArraySetting;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;
import dev.rndmorris.salisarcana.lib.IntegerHelper;

public class EnhancementsModule extends BaseConfigModule {

    public final ToggleSetting lessPickyPrimalCharmRecipe;
    public final ToggleSetting rotatedFociRecipes;
    public final ToggleSetting rotatedThaumometerRecipe;

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

    public final Setting nomiconScrollwheelEnabled;
    public final Setting nomiconInvertedScrolling;
    public final Setting nomiconRightClickClose;
    public final Setting nomiconShowResearchId;

    public final ToggleSetting stabilizerRewrite;
    public final BlockItemListSetting<Integer> stabilizerAdditions;
    public final BlockItemListSetting<Object> stabilizerExclusions;
    public final IntSetting stabilizerStrength;

    public final IntSetting itemEldritchObjectStackSize;

    public final ToggleSetting wandPedestalUseCV;

    public EnhancementsModule() {
        // spotless:off
        addSettings(
            lessPickyPrimalCharmRecipe = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "friendlyPrimalCharm",
                "Make the primal charm's crafting recipe less picky about the order in which primal shards are placed in the top and bottom rows.")
                    .setCategory("recipes"),
            rotatedFociRecipes = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "rotatedFoci",
                "Add rotated recipes for the fire, shock, frost, equal rade, excavation, and primal wand foci.")
                    .setCategory("recipes"),
            rotatedThaumometerRecipe = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "rotatedThaumometer",
                "Add a rotated crafting recipe for the Thaumometer.").setCategory("recipes"),
            lookalikePlanks = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "enableLookalikePlanks",
                "Add look-a-like greatwood and silverwood planks that behave as normal planks, instead of the weirdness of TC4's planks."),
            nodeModifierWeights = new IntArraySetting(
                this,
                ConfigPhase.EARLY,
                "nodeModifierWeights",
                "Node Modifier Worldgen Weights (normal, bright, pale, fading)",
                // calculated based on TC4's default `specialNodeRarity` value
                new int[] { 972222, 9259, 9259, 9259, },
                0,
                1000000).setEnabled(false),
            nodeTypeWeights = new IntArraySetting(
                this,
                ConfigPhase.EARLY,
                "nodeTypeWeights",
                "Node Type Worldgen Weights (normal, unstable, dark, pure, hungry)",
                // calculated based on TC4's default `specialNodeRarity` value
                new int[] { 944444, 16666, 16666, 16666, 5555, },
                0,
                1000000).setEnabled(false),
            suppressWarpEventsInCreative = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "suppressWarpEventsInCreative",
                "Prevent random warp events from firing for players in creative mode."),
            useAllBaublesSlots = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "useAllBaublesSlots",
                "Enables support for mods that increase the number of baubles slots."),
            nomiconInvertedScrolling = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Inverse Scrolling",
                "While viewing the Thaumonomicon, inverts the scrolling for tab switching").setEnabled(false),
            nomiconRightClickClose = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Right-Click Navigation",
                "While viewing the Thaumonomicon, right clicking in a research will take you back to the previous research, or back to the Thaumonomicon."),
            nomiconScrollwheelEnabled = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Enable Scrollwheel",
                "While viewing the Thaumonomicon, enables ctrl + scroll to quick switch tabs"),
            nomiconShowResearchId = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Show Research Key",
                "While viewing the Thaumonomicon, allows you to view the internal name of a research while hovering over it and holding control"),
            wandPedestalUseCV = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Wand Pedestal CV Support",
                "Allows wand pedestals to draw from centivis instead of just regular nodes"),
            stabilizerRewrite = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "useStabilizerRewrite",
                "Rewrites the Runic Matrix's surroundings-check logic to be more flexible when checking for pedestals and stabilizers.")
                    .setCategory("infusion")
                    .setEnabled(false),
            stabilizerStrength = (IntSetting) new IntSetting(
                stabilizerRewrite,
                ConfigPhase.LATE,
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
                ConfigPhase.EARLY,
                "eldritchObjectStackSize",
                "The maximum stack size for Eldritch Objects (Primordial Pearl, Eldritch Eye, Crimson Rites, Eldritch Obelisk Placer, Runed Tablet).",
                16).setMinValue(1)
                    .setMaxValue(64),
            stopCreativeModeItemConsumption = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "stopCreativeModeItemConsumption",
                "Prevent eldritch eyes and phials of essentia from being consumed when used in creative mode."),
            infiniteCreativeVis = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "infiniteCreativeVis",
                "Allow wands to have infinite vis in creative mode."),
            manaPodGrowthRate = new IntSetting(
                this,
                ConfigPhase.EARLY,
                "manaBeanGrowthChance",
                "The chance for a mana bean to grow when a mana pod is updated. Lower values are more likely to grow, with 0 growing every random tick.",
                30).setMinValue(0)
                    .setMaxValue(100),
            thaumicInventoryScanning = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "thaumicInventoryScanning",
                "Enable the ability to scan items in inventories using the Thaumometer. Replaces the Thaumic Inventory Scanning mod."),
            thaumcraftCommandTabCompletion = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "thaumcraftCommandTabCompletion",
                "Enable tab completion for Thaumcraft commands.")

        );

        // spotless:on
        // noinspection unchecked
        addSettings(
            stabilizerAdditions = (BlockItemListSetting<Integer>) new BlockItemListSetting<Integer>(
                stabilizerRewrite,
                ConfigPhase.LATE,
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
        // noinspection unchecked
        addSettings(
            stabilizerExclusions = (BlockItemListSetting<Object>) new BlockItemListSetting<Object>(
                stabilizerRewrite,
                ConfigPhase.LATE,
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
