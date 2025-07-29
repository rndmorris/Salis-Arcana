package dev.rndmorris.salisarcana.config.group;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.BeaconBlockFixSetting;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class ConfigBugfixes extends ConfigGroup {

    public final ToggleSetting advAlchemicalFurnaceSaveNbt = new ToggleSetting(
        this,
        "advAlchemicalFurnaceSaveNbt",
        "Saves additional NBT data to the advanced alchemical furnace for more consistent behavior.");

    public final BeaconBlockFixSetting beaconBlockFixSetting = new BeaconBlockFixSetting(this);

    public final ToggleSetting candleRendererCrashes = new ToggleSetting(
        this,
        "candleMetadataCrash",
        "Fixes several crashes with invalid candle metadata");

    public final ToggleSetting deadMobsDontAttack = new ToggleSetting(
        this,
        "deadMobsDontAttack",
        "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation.");

    public final ToggleSetting fixEFRRecipes = new ToggleSetting(
        this,
        "fixEFRRecipes",
        "Fixes several recipes to work with EFR versions of blocks, like trapdoors, if EFR is installed");

    public final ToggleSetting infernalFurnaceDupeFix = new ToggleSetting(
        this,
        "infernalFurnaceDupeFix",
        "Fixes a smelting duplication glitch with the Infernal Furnace");

    public final ToggleSetting integerInfusionMatrixMath = new ToggleSetting(
        this,
        "integerInfusionMatrixMath",
        "Calculate infusion stabilizers with integer math instead of floating-point math. This eliminates a rounding error that sometimes makes an infusion altar slightly less stable than it should be. Also corrects a logic error causing the Infusion Matrix to check the wrong coordinates for a symmetrical stabilizer.");

    public final ToggleSetting itemMetadataSafetyCheck = new ToggleSetting(
        this,
        "itemMetadataFix",
        "Add a safety check to several Thaumcraft items to prevent crashes when creating those items with invalid metadata.");

    public final ToggleSetting itemShardColor = new ToggleSetting(
        this,
        "shardMetadataCrash",
        "Fixes a crash with invalid shard metadata");

    public final ToggleSetting warpFakePlayerCheck = new ToggleSetting(
        this,
        "warpFakePlayerCheck",
        "Adds a safety check to prevent warp effects from trying to send packets to fake players.");

    public final ToggleSetting crimsonRitesFakePlayerCheck = new ToggleSetting(
        this,
        "crimsonRitesFakePlayerCheck",
        "Adds a safety check in case of a fake player not being castable to EntityPlayerMP.");

    public final ToggleSetting renderRedstoneFix = new ToggleSetting(
        this,
        "renderRedstoneFix",
        "Fixes an issue with ores where they don't get rendered as normal blocks, not allowing you to push a redstone signal through them.");

    public final ToggleSetting slabBurnTimeFix = new ToggleSetting(
        this,
        "slabBurnTimeFix",
        "Reduce the burn time of Thaumcraft's greatwood and silverwood slabs to match that of Minecraft's wooden slabs.");

    public final ToggleSetting strictInfusionMatrixInputChecks = new ToggleSetting(
        this,
        "strictInfusionMatrixInputChecks",
        "Check the infusion matrix's center item more strictly. Prevents an exploit with infusion enchanting.");

    public final ToggleSetting unOredictGoldCoin = new ToggleSetting(
        this,
        "unOredictGoldCoin",
        "Remove gold coins from the gold nugget ore dictionary.").setEnabled(false);

    public final ToggleSetting staffFocusEffectFix = new ToggleSetting(
        this,
        "staffFocusEffectFix",
        "Fixes a graphical error where focus effects would appear below the tip of a staff.");

    public final ToggleSetting arcaneWorkbenchGhostItemFix = new ToggleSetting(
        this,
        "arcaneWorkbenchGhostItemFix",
        "Fixes ghost items being crafted in the arcane workbench after the wand runs out of vis during a shift-click craft.");

    public final ToggleSetting arcaneWorkbenchAllowRechargeCrafting = new ToggleSetting(
        this,
        "arcaneWorkbenchAllowRechargeCrafting",
        "Allows players to craft after the wand in the GUI runs out of vis and is recharged by a Vis Charge Relay.");

    public final ToggleSetting arcaneWorkbenchMultiContainer = new ToggleSetting(
        this,
        "arcaneWorkbenchMultiContainer",
        "Prevents bugs related to multiple players opening an Arcane Workbench's GUI at the same time.");

    public final ToggleSetting negativeBossSpawnCount = new ToggleSetting(
        this,
        "negativeBossSpawnCount",
        "Fixes a theoretical bug where, if billions of bosses were spawned, only the golem boss would be able to spawn.");

    public final ToggleSetting useForgeFishingLists = new ToggleSetting(
        this,
        "useForgeFishingLists",
        "Use Forge's fishing lists to determine what a fishing golem catches.");

    public final ToggleSetting focalManipulatorForbidSwaps = new ToggleSetting(
        this,
        "focalManipulatorForbidSwaps",
        "Prevents players from putting on conflicting or out-of-order upgrades onto a focus by swapping the focus being modified during the upgrade process.");

    public final ToggleSetting equalTradeBreaksBlocks = new ToggleSetting(
        this,
        "equalTradeBreaksBlocks",
        "Fixes a bug where you couldn't break blocks if you were holding the equal trade focus item.");

    public final ToggleSetting nodesRechargeInGameTime = new ToggleSetting(
        this,
        "nodesRechargeInGameTime",
        "Unloaded nodes will regenerate based on game time, not real life time.");

    public final ToggleSetting nodesRememberBeingDrained = new ToggleSetting(
        this,
        "nodesRememberBeingDrained",
        "Nodes will remember being drained, preventing rapidly loading, draining, then unloading nodes exploiting nodes' catch-up recharging.");

    public final ToggleSetting silverwoodLogCorrectName = new ToggleSetting(
        this,
        "silverwoodLogCorrectName",
        "Non-vertical silverwood logs will be correctly named \"Silverwood Log\" in WAILA.");

    public final ToggleSetting updateBiomeColorRendering = new ToggleSetting(
        this,
        "updateBiomeColorRendering",
        "Biome changes will correctly update the color of grass in chunks without needing a block to change.");

    public final ToggleSetting preventBlockAiryFluidReplacement = new ToggleSetting(
        this,
        "preventBlockAiryFluidReplacement",
        "Prevents useful airy blocks (nodes, energized nodes, and the blocks of the Outer Lands boss room door) from being replaced by buckets with liquid.");

    public final ToggleSetting runedStoneIgnoreCreative = new ToggleSetting(
        this,
        "runedStoneIgnoreCreative",
        "Runed Stone (shock traps in Outer Lands) will not attempt to shock players in Creative Mode.");

    public final ToggleSetting upgradedFocusVisCost = new ToggleSetting(
        this,
        "upgradedFocusVisCost",
        "Makes certain upgraded foci (ex. Wand Focus: Fire with Fireball upgrade) spend the upgraded vis cost rather than the default.");

    public final ToggleSetting jarNoCreativeDrops = new ToggleSetting(
        this,
        "jarNoCreativeDrops",
        "Prevent Warded Jars and Node in a Jar from dropping items when broken in Creative.");

    public final ToggleSetting bannerNoCreativeDrops = new ToggleSetting(
        this,
        "bannerNoCreativeDrops",
        "Prevent Banners from dropping items when broken in Creative.");

    public final ToggleSetting bannerPickBlock = new ToggleSetting(
        this,
        "bannerPickBlock",
        "Causes the banner to give the actual banner item when pick-block is used, instead of giving a Crimson Cult Banner. Also fixes the icon of the banner in WAILA.");

    public final ToggleSetting jarPickBlock = new ToggleSetting(
        this,
        "jarPickBlock",
        "Causes Warded Jars and Node in a Jar to create an item with the current contents of the jar when pick-block is used. Also fixes the WAILA tooltip for those blocks.");

    public final ToggleSetting correctItemInsertion = new ToggleSetting(
        this,
        "correctItemInsertion",
        "Thaumcraft will correctly insert items into inventories - prevents double-counting slots when testing for space and allows insertion of items into an empty slot of the other side of a double chest.");

    public final ToggleSetting lootBlockHitbox = new ToggleSetting(
        this,
        "lootBlockHitbox",
        "Correctly sets the hitboxes of the Old Urn & Abandoned Crate, preventing a bug where you can phase through the blocks while mining them.");

    public final ToggleSetting etherealBloomSaveNBT = new ToggleSetting(
        this,
        "etherealBloomSaveNBT",
        "Thaumcraft will correctly save ethereal bloom counters to disk, preventing the growth animation from looping on relog and stopping the cleanse timer from restarting.");

    public final ToggleSetting silkTouchCrystalClusters = new ToggleSetting(
        this,
        "silkTouchCrystalClusters",
        "Allows Thaumcraft crystal clusters to be harvested with Silk Touch, preventing them from dropping as shards.");

    public final ToggleSetting thaumatoriumMultiContainer = new ToggleSetting(
        this,
        "thaumatoriumMultiContainer",
        "Fixes some possible bugs caused by multiple players interacting with a Thaumatorium at the same time.");

    public final ToggleSetting fixClientSideLocalization = new ToggleSetting(
        this,
        "fixClientSideLocalization",
        "Make all messages displayed get localized on the client.");

    public final ToggleSetting excavationFocusDeterministicCost = new ToggleSetting(
        this,
        "excavationFocusDeterministicCost",
        "Causes the upgraded vis cost of Wand Focus: Excavation to be dependent solely on the applied upgrade rather than which upgrade loaded first in any game session.");

    public final ToggleSetting bannerReturnPhials = new ToggleSetting(
        this,
        "bannerReturnPhials",
        "Applying patterns to banners will only consume the essentia, and not the phial in which it is stored.");

    @Nonnull
    @Override
    public String getGroupName() {
        return "bugfixes";
    }

    @Nonnull
    @Override
    public String getGroupComment() {
        return "Fixes for bugs in TC4";
    }

}
