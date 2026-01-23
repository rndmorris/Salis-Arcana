# Bugfixes

Config file: `config/salisarcana/bugfixes.cfg`

## Advanced Alchemical Furnace - Save Additional NBT Data

**Config option:** `advAlchemicalFurnaceSaveNbt`

The Advanced Alchemical Furnace will save and load additional NBT data needed to remember how recently it requested
centivis and its delay between melting items.

## Beacon Blocks

**Config option:** `blockCosmeticSolidBeaconIds`

Configure which variants of BlockCosmeticSolid (tallow blocks, arcane stone blocks/bricks, obsidian tiles,
thaumium blocks, etc.) are considered valid beacon blocks. By default, this setting makes Thaumium Blocks
the only valid variant.

## Dying Mobs No Longer Attack

**Config option:** `deadMobsDontAttack`

Prevent eldritch crabs, thaumic slimes, and all taintacles from being able to attack while performing their death
animation.

## Infernal Furnace Item Duplication Fix

**Config option:** `infernalFurnaceDupeFix`

Prevent the Infernal Furnace from duplicating inputs when items are spam-tossed into it.

## Infusion Matrix Fixes

**Config option:** `integerInfusionMatrixMath`

Calculate infusion stabilizers with integer math instead of floating-point math. This eliminates a rounding error that
sometimes makes an infusion altar slightly less stable than it should be.

**Config option:** `strictInfusionMatrixInputChecks`

Check the infusion matrix's center item more strictly. Prevents an exploit with infusion enchanting.

## Nonnegative Boss Spawn Count

**Config option:** `negativeBossSpawnCount`

Fixes a theoretical bug where, if billions of bosses were spawned, only the golem boss would be able to spawn.

## Metadata Safety Checks

**Config option:** `candleMetadataCrash`

Add a safety check to prevent the creation of Thaumcraft candles with invalid metadata.

**Config option:** `itemMetadataFix`

Add a safety check to several Thaumcraft items to prevent crashes when creating those items with invalid metadata.

Affected items include:
* Treasure bags
* Nuggets and clusters
* Resource items (alumentum, tallow, etc)
* Runic baubles
* Primal arrows
* Wand caps and rods

**Config option:** `shardMetadataCrash`,

Add a safety check to prevent the creation of Thaumcraft shards with invalid metadata.

## Fake Player Safety Checks

**Config option:** `warpFakePlayerCheck`

Adds a safety check to prevent warp effects from trying to send packets to fake players.

**Config option:** `crimsonRitesFakePlayerCheck`

Adds a safety check in case of a fake player not being castable to `EntityPlayerMP`. (For example, putting Crimson Rites into a Dynamism Tablet from Thaumic Tinkerer.)

## TC4 Ores Redstone Fix

**Config option:** `renderRedstoneFix`

Update Thaumcraft ore blocks so they can convey a redstone signal.

## Wooden Slab Burn Time

**Config option:** `slabBurnTimeFix`

Reduce the burn time of Thaumcraft's greatwood and silverwood slabs to match that of Minecraft's wooden slabs.

## Staff Foci Effect Fixes

**Config option:** `staffFocusEffectFix`

Fixes a graphical error where focus effects would appear below the tip of a staff.

## Cancel Focal Manipulator Upon Stack Swap

**Config option:** `focalManipulatorForbidSwaps`

Prevents players from putting on conflicting or out-of-order upgrades onto a focus by swapping the focus being modified during the upgrade process.

## Arcane Workbench Fixes

### Fix Ghost Items After Shift-Click Crafting

**Config option:** `arcaneWorkbenchGhostItemFix`

Fixes ghost items being crafted in the arcane workbench after the wand runs out of vis during a shift-click craft.

### Allow Crafting After Wand Runs Out Of Vis & Is Recharged

**Config option:** `arcaneWorkbenchAllowRechargeCrafting`

Allows players to craft after the wand in the GUI runs out of vis and is recharged by a Vis Charge Relay.

### Multiplayer Duplication Bugfix & Per-Player Output Slots

**Config option:** `arcaneWorkbenchMultiContainer`

Prevents bugs related to multiple players opening an Arcane Workbench's GUI at the same time, including a duplication bug, research verification bug, and some others.

### Cache Last Recipe in Arcane Workbench

**Config option:** `arcaneWorkbenchCache`

Causes each Arcane Workbench to keep a cache of the last valid recipe, massively improving the performance of bulk crafts & the GUI.

### Arcane Workbench Pretends to be a Crafting Table during Forge Events

**Config option:** `arcaneWorkbenchForgeEventBridge`

When performing a mundane crafting recipe in an Arcane Workbench, it will pretend to be a normal crafting table when sending the Forge event. Prevents the Spellbinding Cloth item duplication glitch.

## Use Forge fishing lists for fishing golem loot

**Config option:** `useForgeFishingLists`

Use Forge's fishing lists to determine what fish, junk, and treasure a fishing golem catches.

## Rotated Silverwood Logs Display Correct Name in WAILA

**Config option:** `silverwoodLogCorrectName`

Non-vertical silverwood logs will be correctly named "Silverwood Log" in WAILA.

## Visually Update Biome Colors

**Config option:** `updateBiomeColorRendering`

Biome changes will correctly update the color of grass in chunks without needing a block to change.

## Prevent Nodes & Boss Doors from being broken by filled Buckets

**Config option:** `preventBlockAiryFluidReplacement`

Prevents useful airy blocks (nodes, energized nodes, and the blocks of the Outer Lands boss room door) from being replaced by buckets with liquid.

## Prevent Runed Stone from Shocking Creative Players

**Config option:** `runedStoneIgnoreCreative`

Runed Stone (shock traps in Outer Lands) will not attempt to shock players in Creative Mode.

## Spend the Correct Vis Cost of Upgraded Foci

**Config option:** `upgradedFocusVisCost`

Makes certain upgraded foci (ex. Wand Focus: Fire with Fireball upgrade) spend the upgraded vis cost rather than the default.

## Deterministic Vis Cost of Wand Focus: Excavation

**Config option:** `excavationFocusDeterministicCost`

Causes the upgraded vis cost of Wand Focus: Excavation to be dependent solely on the applied upgrade rather than which upgrade loaded first in any game session.

## Prevent Blocks From Dropping When Broken In Creative

**Config option:** `jarNoCreativeDrops`

Prevent Warded Jars and Node in a Jar from dropping items when broken in Creative.

**Config option:** `bannerNoCreativeDrops`

Prevent Banners from dropping items when broken in Creative.

## Pick-Block Banners Accurately

**Config option:** `bannerPickBlock`

Causes the banner to give the actual banner item when pick-block is used, instead of giving a Crimson Cult Banner. Also fixes the icon of the banner in WAILA.

## Pick-Block Warded Jars Accurately

**Config option:** `jarPickBlock`

Causes Warded Jars and Node in a Jar to create an item with the current contents of the jar when pick-block is used, rather than an empty jar. Also fixes the WAILA tooltip for those blocks.

## Correct Item Insertion Logic

**Config option:** `correctItemInsertion`

Thaumcraft will correctly insect items into inventories - prevents double-counting slots when testing for space and allows insertion of items into an empty slot of the other side of a double chest.

## Ethereal Bloom Growth Animation Fix

**Config Option:** `etherealBloomSaveNBT`

Thaumcraft will correctly save ethereal bloom counters to disk, preventing the growth animation from looping on relog and stopping the cleanse timer from restarting.

## Silk Touch Crystal Clusters

**Config option:** `silkTouchCrystalClusters`

Allows Thaumcraft crystal clusters to be harvested with Silk Touch, preventing them from dropping as shards.

## Abandoned Crates & Old Urn Hitboxes

**Config option:** `lootBlockHitbox`

Correctly sets the hitboxes of the Old Urn & Abandoned Crate, preventing a bug where you can phase through the blocks while mining them.

## Thaumatorium Multiplayer Bug-fix

**Config option:** `thaumatoriumMultiContainer`

Makes the server correctly handle multiple players interacting with a Thaumatorium at the same time and cleans up client-side `Container`s when the player closes the GUI.

## Localize Chat Messages on the Client

**Config option:** `fixClientSideLocalization`

All messages displayed in chat will be displayed in the client's language rather than the language of the server.

## Prevent Consumption of Phials when Applied to Banners

**Config option:** `bannerReturnPhials`

Applying patterns to banners will only consume the essentia, and not the phial in which it is stored.

## Arcane Key Extra Security

**Config option:** `extraSecureArcaneKeys`

Arcane Keys will now save the dimension and the creator of the key when linked to a warded object, and will check those values before granting permission.

## Earth Shock Blocks Require Solid Ground

**Config option:** `earthShockRequireSolidGround`

Requires the spark blocks left behind by Wand Focus: Shock with the Earth Shock upgrade to have a solid block beneath them to exist.

## Prevent "Unknown Wand Part" Crashes

**Config option:** `unknownWandComponentSupport`

Prevents wand items from crashing everything when they contain a component which hasn't been registered by any mod.

## Prevent Wand Vis GUI Bars from Overflowing

**Config option:** `clampWandOverlayVis`

Prevents the "Vis in Wand" GUI when the wand is held from showing impossible bars when the wand holds impossible amounts of vis.

## Extend Upgrade Focus Packet

**Config option:** `extendUpgradeFocusPacket`

Use a larger packet for sending the ID of the focus upgrade being selected, allowing the use of focus upgrade IDs > 127 on multiplayer servers.

## Terminate Crucible Item-Stack Melting Early

**Config option:** `earlyTerminateCrucibleCraft`

Prevent large item stacks from partially dissolving into aspects if the Crucible runs out of water while crafting.

## Fix TeleporterThaumcraft memory leak

**Config option:** `fixTeleporterThaumcraftLeak`

Prevents a world object memory leak in the TeleporterThaumcraft class.

## Prevent Warp Sounds from Blasting Out Your Eardrums

**Config option:** `muteExcessiveWarpSounds`

Prevent warp sounds from blasting out your eardrums when you obtains lots of warp in an instant.

## Handle Invalid Focus NBT on Wands

**Config option:** `preventInvalidFociOnWands`

Prevent wands with invalid foci NBT (from uninstalling add-ons, for example) from crashing the game.

## Pickaxe of the Core (Group `tools`)

**Config option:** `detectZeroAspectBlocks`

Fixes an oversight with the pickaxe of the core that prevents it from displaying detected blocks that have no aspects.

**Config option:** `detectLitRedstoneOre`

Works around limitation with Forge that prevents the pickaxe of the core fom detecting lit redstone ore by treating it
as identical to regular redstone ore.

**Config option:** `detectLapisOre`

Allows the pickaxe of the core to correctly detect vanilla lapis ore.

Fixes a bug with the pickaxe of the core that caused it to rely on the damage value of the block's dropped item to
identify a block instead of its in-world metadata when scanning for ores.

## Hidden Research Notes

**Config option:** `hiddenResearchUseWorldRandom`

When right-clicking a Research Note made from Knowledge Fragments, will use a random value not dependent on the world's time of day.

**Config option:** `hiddenResearchCheckInventory`

When right-clicking a Research Note made from Knowledge Fragments, will not generate a research note you already have in your inventory.

## Prevent Crucible Dead-Item Duplication Glitch

**Config option:** `crucibleDeadItemDupe`

Prevent multiple Crucibles from melting/crafting the same item entity in the same tick.

## Golem Visors affect Golem Dart Launchers

**Config option:** `golemVisorAffectDartLauncher`

Golems equipped with Dart Launchers & Visors will be able to cause player kills using their darts.

## Prevent Golem Held-Item Duplication

**Config option:** `preventGolemDropDuplication`

Prevent players from disassembling golems that have already been killed in order to duplicate their held item.

## Stable Research Altar Animation

**Config option:** `stableRunicMatrixAnimation`

Runic Matrices which are too stable will not fly far out from the center of the multiblock.
