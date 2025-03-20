package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

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
    public final ToggleSetting warpFakePlayerCheck;
    public final ToggleSetting crimsonRitesFakePlayerCheck;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting slabBurnTimeFix;
    public final ToggleSetting strictInfusionMatrixInputChecks;
    public final ToggleSetting unOredictGoldCoin;
    public final ToggleSetting staffFocusEffectFix;
    public final ToggleSetting arcaneWorkbenchGhostItemFix;
    public final ToggleSetting arcaneWorkbenchAllowRechargeCrafting;
    public final ToggleSetting negativeBossSpawnCount;
    public final ToggleSetting useForgeFishingLists;

    public BugfixesModule() {
        addSettings(
            beaconBlockFixSetting = new BeaconBlockFixSetting(this),
            candleRendererCrashes = new ToggleSetting(
                this,
                "candleMetadataCrash",
                "Fixes several crashes with invalid candle metadata"),
            deadMobsDontAttack = new ToggleSetting(
                this,
                "deadMobsDontAttack",
                "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation."),
            fixEFRRecipes = new ToggleSetting(
                this,
                "fixEFRRecipes",
                "Fixes several recipes to work with EFR versions of blocks, like trapdoors, if EFR is installed"),
            infernalFurnaceDupeFix = new ToggleSetting(
                this,
                "infernalFurnaceDupeFix",
                "Fixes a smelting duplication glitch with the Infernal Furnace"),
            integerInfusionMatrixMath = new ToggleSetting(
                this,
                "integerInfusionMatrixMath",
                "Calculate infusion stabilizers with integer math instead of floating-point math. This eliminates a rounding error that sometimes makes an infusion altar slightly less stable than it should be. Also corrects a logic error causing the Infusion Matrix to check the wrong coordinates for a symmetrical stabilizer."),
            itemMetadataSafetyCheck = new ToggleSetting(
                this,
                "itemMetadataFix",
                "Add a safety check to several Thaumcraft items to prevent crashes when creating those items with invalid metadata."),
            itemShardColor = new ToggleSetting(this, "shardMetadataCrash", "Fixes a crash with invalid shard metadata"),
            warpFakePlayerCheck = new ToggleSetting(
                this,
                "warpFakePlayerCheck",
                "Adds a safety check to prevent warp effects from trying to send packets to fake players."),
            crimsonRitesFakePlayerCheck = new ToggleSetting(
                this,
                "crimsonRitesFakePlayerCheck",
                "Adds a safety check in case of a fake player not being castable to EntityPlayerMP."),
            renderRedstoneFix = new ToggleSetting(
                this,
                "renderRedstoneFix",
                "Fixes an issue with ores where they don't get rendered as normal blocks, not allowing you to push a redstone signal through them."),
            slabBurnTimeFix = new ToggleSetting(
                this,
                "slabBurnTimeFix",
                "Reduce the burn time of Thaumcraft's greatwood and silverwood slabs to match that of Minecraft's wooden slabs."),
            strictInfusionMatrixInputChecks = new ToggleSetting(
                this,
                "strictInfusionMatrixInputChecks",
                "Check the infusion matrix's center item more strictly. Prevents an exploit with infusion enchanting."),
            unOredictGoldCoin = new ToggleSetting(
                this,
                "unOredictGoldCoin",
                "Remove gold coins from the gold nugget ore dictionary.").setEnabled(false),
            staffFocusEffectFix = new ToggleSetting(
                this,
                "staffFocusEffectFix",
                "Fixes a graphical error where focus effects would appear below the tip of a staff."),
            arcaneWorkbenchGhostItemFix = new ToggleSetting(
                this,
                "arcaneWorkbenchGhostItemFix",
                "Fixes ghost items being crafted in the arcane workbench after the wand runs out of vis during a shift-click craft."),
            arcaneWorkbenchAllowRechargeCrafting = new ToggleSetting(
                this,
                "arcaneWorkbenchAllowRechargeCrafting",
                "Allows players to craft after the wand in the GUI runs out of vis and is recharged by a Vis Charge Relay."),
            negativeBossSpawnCount = new ToggleSetting(
                this,
                "negativeBossSpawnCount",
                "Fixes a theoretical bug where, if billions of bosses were spawned, only the golem boss would be able to spawn."),
            useForgeFishingLists = new ToggleSetting(
                this,
                "useForgeFishingLists",
                "Use Forge's fishing lists to determine what a fishing golem catches."));
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
