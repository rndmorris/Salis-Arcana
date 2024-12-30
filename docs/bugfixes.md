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

## Metadata Safety Checks

**Config option:** `candleMetadataCrash`

Add a safety check to prevent the creation of Thaumcraft candles with invalid metadata.

**Config option:** `itemLootBagMetadata`

Add a safety check to prevent crashes when creating a treasure bag with invalid metadata.

**Config option:** `shardMetadataCrash`,

Add a safety check to prevent the creation of Thaumcraft shards with invalid metadata.

## TC4 Ores Redstone Fix

**Config option:** `renderRedstoneFix`

Update Thaumcraft ore blocks so they can convey a redstone signal.
