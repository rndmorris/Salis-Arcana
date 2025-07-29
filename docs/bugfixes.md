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
