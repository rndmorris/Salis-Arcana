# Bugfixes

Config file: `config/salisarcana/bugfixes.cfg`

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

## Arcane Workbench Fixes

### Fix Ghost Items After Shift-Click Crafting

**Config option:** `arcaneWorkbenchGhostItemFix`

Fixes ghost items being crafted in the arcane workbench after the wand runs out of vis during a shift-click craft.

### Allow Crafting After Wand Runs Out Of Vis & Is Recharged

**Config option:** `arcaneWorkbenchAllowRechargeCrafting`

Allows players to craft after the wand in the GUI runs out of vis and is recharged by a Vis Charge Relay.

### Use Forge fishing lists for fishing golem loot

**Config option:** `useForgeFishingLists`

Use Forge's fishing lists to determine what fish, junk, and treasure a fishing golem catches.
