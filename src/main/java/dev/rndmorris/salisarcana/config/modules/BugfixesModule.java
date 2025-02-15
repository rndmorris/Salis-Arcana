package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.BeaconBlockFixSetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class BugfixesModule extends BaseConfigModule {

    public final BeaconBlockFixSetting beaconBlockFixSetting;
    public final ToggleSetting candleRendererCrashes;
    public final ToggleSetting deadMobsDontAttack;
    public final ToggleSetting fixEFRRecipes;
    public final ToggleSetting infernalFurnaceDupeFix;
    public final ToggleSetting integerInfusionMatrixMath;
    public final ToggleSetting itemMetadataSafetyCheck;
    public final ToggleSetting itemShardColor;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting slabBurnTimeFix;
    public final ToggleSetting strictInfusionMatrixInputChecks;
    public final ToggleSetting unOredictGoldCoin;
    public final ToggleSetting staffFocusEffectFix;
    public final ToggleSetting arcaneWorkbenchGhostItemFix;
    public final ToggleSetting arcaneWorkbenchAllowRechargeCrafting;

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
            fixEFRRecipes = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "fixEFRRecipes",
                "Fixes several recipes to work with EFR versions of blocks, like trapdoors, if EFR is installed"),
            infernalFurnaceDupeFix = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "infernalFurnaceDupeFix",
                "Fixes a smelting duplication glitch with the Infernal Furnace"),
            integerInfusionMatrixMath = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "integerInfusionMatrixMath",
                "Calculate infusion stabilizers with integer math instead of floating-point math. This eliminates a rounding error that sometimes makes an infusion altar slightly less stable than it should be. Also corrects a logic error causing the Infusion Matrix to check the wrong coordinates for a symmetrical stabilizer."),
            itemMetadataSafetyCheck = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "itemMetadataFix",
                "Add a safety check to several Thaumcraft items to prevent crashes when creating those items with invalid metadata."),
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
            slabBurnTimeFix = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "slabBurnTimeFix",
                "Reduce the burn time of Thaumcraft's greatwood and silverwood slabs to match that of Minecraft's wooden slabs."),
            strictInfusionMatrixInputChecks = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "strictInfusionMatrixInputChecks",
                "Check the infusion matrix's center item more strictly. Prevents an exploit with infusion enchanting."),
            unOredictGoldCoin = (ToggleSetting) new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "unOredictGoldCoin",
                "Remove gold coins from the gold nugget ore dictionary.").setEnabled(false),
            staffFocusEffectFix = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "staffFocusEffectFix",
                "Fixes a graphical error where focus effects would appear below the tip of a staff."),
            arcaneWorkbenchGhostItemFix = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "arcaneWorkbenchGhostItemFix",
                "Fixes ghost items being crafted in the arcane workbench after the wand runs out of vis during a shift-click craft."),
            arcaneWorkbenchAllowRechargeCrafting = new ToggleSetting(
                this,
                ConfigPhase.LATE,
                "arcaneWorkbenchAllowRechargeCrafting",
                "Allows players to craft after the wand in the GUI runs out of vis and is recharged by a Vis Charge Relay."));
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
