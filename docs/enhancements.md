# Enhancements

Config file: `config/salisarcana/enhancements.cfg`

## Additional Bauble Slot Support

**Config option:** `useAllBaublesSlots`

All bauble slots, including those added by mods that extend Baubles' API, will be checked when
calculating vis discounts, runic armor, and focus pouches containing wand foci.

## Configurable Biome Colors
Want tainted lands to look like The Crimson? Want the eerie biome that manifests around sinster nodes to bleach the world of color?

[Give the biome color module a try](./biome-colors.md)

## Look-a-like Plank Blocks

**Config option:** `enableLookalikePlanks`

Adds decorative Greatwood and Silverwood plank blocks that behave as normal blocks should. Allows using those textures
while avoiding the weirdness in Thaumcraft's plank blocks implementation.

## Suppress Warp Events in Creative Mode

**Config option:** `suppressWarpEventsInCreative`

Prevent random warp events from occurring for players in creative mode.

## Thaumonomicon Controls

**Config option:**: `Enable Scrollwheel`

Ctrl + scroll the quick switch tabs in the Thaumonomicon.

**Config option:** `Inverse Scrolling`

Invert the scrolling for tab switching, if `Enable Scrollwheel` is true.

**Config option:** `Right-Click Navigation`

Right-clicking while viewing research will navigate back to the previous research, or to the Thaumonomicon if there
is no research to navigate back to.

**Config option:** `Show Research Key`

Holding Ctrl while hovering over research in the Thaumonomicon will display that research's internal id.

## Weighted Node Generation

**Config option:** `enableNodeModifierWeights`

If enabled, randomly generated nodes will pick their modifier from the weighted list specified in the `nodeModifierWeights` config option.

**Config option:** `nodeModifierWeights`

The weights used to pick the modifier of nodes when randomly generated.

**Config option:** `enableNodeTypeWeights`

If enabled, randomly generated nodes will pick their type from the weighted list specified in the `nodeTypeWeights` config option.

**Config option:** `nodeTypeWeights`
The weights used to pick the type of nodes when randomly generated.


# Enhancements - Infusion

## Stabilizer Additions

### Config option: `enableStabilizerAdditions`

If enabled, blocks specified in `stabilizerAdditions` will be treated as infusion stabilizers, even if they normally would not.

### Config option: `stabilizerAdditions`

A list of blocks that should be treated as infusion stabilizers. Each block should be on its own line, and be in the
format `modId:blockId` or `modId:blockId:metadata`. If `:metadata` is not specified, metadata will be ignored when
determining if a block should be included.

<ins>Example:</ins>

* `minecraft:lapis_block` will include Minecraft's Lapis Lazuli Block
* `Thaumcraft:blockCosmeticSolid` will include Thaumcraft's Obsidian Totem, Obsidian Tile, Paving Stone of Travel/Warding, Thaumium Blocks, etc.
* `Thaumcraft:blockCosmeticSolid:0` will include Thaumcraft's Obsidian Totem, but none of the block's other variants.

## Stabilizer Exclusions

### Config option: `enableStabilizerExclusions`

If enabled, blocks specified in `stabilizerExclusions` will ***NOT*** be treated as infusion stabilizers, even if they normally would.

### Config option: `stabilizerExclusions`

A list of blocks that should *not* be treated as infusion stabilizers, even if they normally would. Each block should be on its own line, and be in the
format `modId:blockId` or `modId:blockId:metadata`. If `:metadata` is not specified, metadata will be ignored when
determining if a block should be excluded.

<ins>Example:</ins>

* `minecraft:skull` will exclude all of Minecraft's skulls and heads
* `minecraft:skull:2` will exclude Minecraft's Zombie Head
* `Thaumcraft:blockCandle` will exclude all Thaumcraft candles

## How Additions and Exclusions Interact

If a block matches an entry on the Additions list, it is *always* treated as an infusion stabilizer, even if it also
appears on the Exclusions list. This interaction can be used to succinctly exclude only some existing stabilizers.

<ins>Example:</ins>

If the Additions list contains `Thaumcraft:blockCandle:10` (Purple Tallow Candles), and the Exclusions list contains
`Thaumcraft:blockCandle` (all Tallow Candles), Purple Tallow Candles will help stabilize an infusion but other colors
will not.

