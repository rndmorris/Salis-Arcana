package dev.rndmorris.tfixins.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.settings.IntArraySetting;
import dev.rndmorris.tfixins.config.settings.Setting;
import dev.rndmorris.tfixins.config.settings.ToggleSetting;

public class EnhancementsModule extends BaseConfigModule {

    public final ToggleSetting lookalikePlanks;
    public final IntArraySetting nodeModifierWeights;
    public final IntArraySetting nodeTypeWeights;
    public final ToggleSetting suppressWarpEventsInCreative;
    public final ToggleSetting useAllBaublesSlots;

    public final Setting scrollwheelEnabled;
    public final Setting invertedScrolling;
    public final Setting rightClickClose;
    public final Setting showResearchId;

    public EnhancementsModule() {
        addSettings(
            lookalikePlanks = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "enableLookalikePlanks",
                "Add look-a-like greatwood and silverwood planks that behave as normal planks, instead of the weirdness of TC4's planks."),
            nodeModifierWeights = new IntArraySetting(
                this,
                ConfigPhase.EARLY,
                "nodeModifierWeights",
                "Node Modifier Worldgen Weights (bright, pale, fading, normal)",
                new int[] { 0, 0, 0, 0 },
                0,
                100).setEnabled(false),
            nodeTypeWeights = new IntArraySetting(
                this,
                ConfigPhase.EARLY,
                "nodeTypeWeights",
                "Node Type Worldgen Weights (unstable, dark, hungry, pure, normal)",
                new int[] { 0, 0, 0, 0, 0 },
                0,
                100).setEnabled(false),
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
            invertedScrolling = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Inverse Scrolling",
                "Inverts the scrolling for tab switching").setEnabled(false),
            rightClickClose = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Right-Click Navigation",
                "Right clicking in a research will take you back to the previous research, or back to the Thaumonomicon."),
            scrollwheelEnabled = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Enable Scrollwheel",
                "Enables ctrl + scroll to quick switch tabs"),
            showResearchId = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Show Research Key",
                "Allows you to view the internal name of a research while hovering over it and holding control"));
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
