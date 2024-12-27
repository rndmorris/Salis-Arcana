package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.BeaconBlockFixSetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class BugfixesModule extends BaseConfigModule {

    public final BeaconBlockFixSetting beaconBlockFixSetting;
    public final ToggleSetting candleRendererCrashes;
    public final ToggleSetting deadMobsDontAttack;
    public final ToggleSetting infernalFurnaceDupeFix;
    public final ToggleSetting infusionMatrixSymmetryEnhancements;
    public final ToggleSetting itemShardColor;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting strictInfusionMatrixInputChecks;

    public BugfixesModule() {
        addSettings(
            beaconBlockFixSetting = new BeaconBlockFixSetting(this, ConfigPhase.EARLY),
            candleRendererCrashes = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "candleMetadataCrash",
                "Fixes several crashes with invalid candle metadata"),
            deadMobsDontAttack = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "deadMobsDontAttack",
                "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation."),
            infernalFurnaceDupeFix = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "infernalFurnaceDupeFix",
                "Fixes a smelting duplication glitch with the Infernal Furnace"),
            infusionMatrixSymmetryEnhancements = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "integerInfusionMatrixMath",
                "Correct a number of issues with how the Runic Matrix calculates symmetry, including a switch to use integer logic math of floating point, and fixes an issue checking the incorrect coordinates for a stabilizer's symmetrical pair. Incompatible with any other mod that changes the Infusion Altar's internal surroundings-checking logic."),
            itemShardColor = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "shardMetadataCrash",
                "Fixes a crash with invalid shard metadata"),
            renderRedstoneFix = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "renderRedstoneFix",
                "Fixes an issue with ores where they don't get rendered as normal blocks, not allowing you to push a redstone signal through them."),
            strictInfusionMatrixInputChecks = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "strictInfusionMatrixInputChecks",
                "Check the infusion matrix's center item more strictly. Prevents an exploit with infusion enchanting."));
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "bugfixes";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Fixes for bugs in TC4";
    }

}
