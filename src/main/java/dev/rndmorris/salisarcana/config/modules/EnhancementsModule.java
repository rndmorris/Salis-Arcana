package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.BlockItemListSetting;
import dev.rndmorris.salisarcana.config.settings.IntArraySetting;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class EnhancementsModule extends BaseConfigModule {

    public final ToggleSetting lessPickyPrimalCharmRecipe;

    public final ToggleSetting lookalikePlanks;
    public final IntArraySetting nodeModifierWeights;
    public final IntArraySetting nodeTypeWeights;
    public final ToggleSetting suppressWarpEventsInCreative;
    public final ToggleSetting useAllBaublesSlots;

    public final Setting nomiconScrollwheelEnabled;
    public final Setting nomiconInvertedScrolling;
    public final Setting nomiconRightClickClose;
    public final Setting nomiconShowResearchId;

    public final ToggleSetting stabilizerRewrite;
    public final BlockItemListSetting stabilizerAdditions;
    public final BlockItemListSetting stabilizerExclusions;

    public final ToggleSetting wandPedestalUseCV;

    public EnhancementsModule() {
        addSettings(
            lessPickyPrimalCharmRecipe = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "lessPickyPrimalCharmRecipe",
                "Make the primal charm's crafting recipe less picky about the order in which primal shards are placed in the top and bottom rows.")
                    .setCategory("recipes"),
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
            stabilizerRewrite = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "useStabilizerRewrite",
                "Rewrites the Runic Matrix's surroundings-check logic to be more flexible when checking for pedestals and stabilizers.")
                    .setCategory("infusion")
                    .setEnabled(false),
            stabilizerAdditions = (BlockItemListSetting) new BlockItemListSetting(
                stabilizerRewrite,
                ConfigPhase.LATE,
                "stabilizerAdditions",
                "Requires useStabilizerRewrite=true. Blocks specified here will contribute to stabilizing an infusion altar, even if they normally wouldn't. Format: `modId:blockId` or `modId:blockId:metadata`. If not set, metadata defaults to 0. Set metadata to * or 32767 to match all metadata values.")
                    .setListType(BlockItemListSetting.ListType.BLOCKS)
                    .setCategory("infusion"),
            stabilizerExclusions = (BlockItemListSetting) new BlockItemListSetting(
                stabilizerRewrite,
                ConfigPhase.LATE,
                "stabilizerExclusions",
                "Requires useStabilizerRewrite=true. Blocks specified here will NOT contribute to stabilizing an infusion altar, even if they normally would. Format: `modId:blockId` or `modId:blockId:metadata`. If not set, metadata defaults to 0. Set metadata to * or 32767 to match all metadata values.")
                    .setListType(BlockItemListSetting.ListType.BLOCKS)
                    .setCategory("infusion"),
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
                "Allows wand pedestals to draw from centivis instead of just regular nodes"));
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
