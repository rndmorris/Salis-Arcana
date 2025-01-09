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

## Config option: `useStabilizerRewrite`

Rewrites the Runic Matrix's surroundings-check logic to be more flexible when checking for pedestals and stabilizers.
This is a significant change, and will conflict with any other mod that also modifies the matrix's
surroundings-checking logic.

## Config option: `stabilizerStrength`

Requires `useStabilizerRewrite=true`.

The amount (in one-hundredths of a point) of symmetry each stabilizer block adds to an infusion altar. If a stabilizer doesn't have a symmetrical opposite, an equivalent amount of symmetry will be subtracted instead.

Negative strengths will invert a stabilizer's behavior by stabilizing an infusion altar if placed asymmetrically, and destabilizing an altar if placed symmetrically.

The default value of `10` is equivalent to vanilla Thaumcraft's stabilizer strength.

## Config option: `stabilizerAdditions`

Requires `useStabilizerRewrite=true`.

A list of blocks that should be treated as infusion stabilizers, even if they normally would not. Each block should
be on its own line, and be in the format `modId:blockId` or `modId:blockId:metadata` or `modId:blockId:metadata`.

**Metadata:**
* Defaults to 0 if not set.
* If set to * or 32767, all metadata variants of the block will be included.

**Strength:**
* Defaults to `stabilizerStrength` if not set.
* Range: -10000 to 10000

<ins>Example:</ins>

* `minecraft:lapis_block` will include Minecraft's Lapis Lazuli Block
* `Thaumcraft:blockCosmeticSolid` or `Thaumcraft:blockCosmeticSolid:0` will include Thaumcraft's Obsidian Totem, but none of the block's other variants.
* `Thaumcraft:blockCosmeticSolid:*` or `Thaumcraft:blockCosmeticSolid:32767` will include Thaumcraft's Obsidian Totem, Obsidian Tile, Paving Stone of Travel/Warding, Thaumium Blocks, etc.
* `Thaumcraft:blockCosmeticSolid:0:20` will make obsidian totem blocks will contribute twice as much stability as normal stabilizers.
* `minecraft:skull:*:5` will make all vanilla Minecraft skulls contribute half as much stability as normal stabilizers.
* `Thaumcraft:blockCandle:*:0` will make all Thaumcraft candles count as stabilizers for their symmetrical twin, but not contribute any stability themselves.

## Config option: `stabilizerExclusions`

Requires `useStabilizerRewrite=true`.

A list of blocks that should *not* be treated as infusion stabilizers, even if they normally would. Each block should
be on its own line, and be in the format `modId:blockId` or `modId:blockId:metadata`.

If no metadata is specified, a default value of `0` will be used. If metadata is `*` or `32767`, metadata will be
ignored when determining if a block is present on this list.

<ins>Example:</ins>

* `minecraft:skull` or `minecraft:skull:0` will exclude skeleton skulls
* `minecraft:skull:2` will exclude zombie heads
* `minecraft:skull:*` or `minecraft:skull:32767` will exclude all minecraft skulls and heads
* `Thaumcraft:blockCandle:*` or `Thaumcraft:blockCandle:32767` will exclude all Thaumcraft candles

## How Additions and Exclusions Interact

If a block matches an entry on the Additions list, it is *always* treated as an infusion stabilizer, even if it also
appears on the Exclusions list. This interaction can be used to succinctly exclude only some existing stabilizers.

<ins>Example:</ins>

If the Additions list contains `Thaumcraft:blockCandle:10` (Purple Tallow Candles), and the Exclusions list contains
`Thaumcraft:blockCandle:*` (all Tallow Candles), Purple Tallow Candles will help stabilize an infusion but other colors
will not.

# Enhancements - Recipes

## Config option: `friendlyPrimalCharm`
Make the primal charm's crafting recipe less picky about the order in which primal shards are placed in the top and bottom rows.

<ins>Note:</ins> this recipe's changes will not appear in NEI.

## Config option: `rotatedFoci`
Add rotated recipes for the fire, shock, frost, equal trade, excavation, and primal wand foci.

## Config option: `rotatedThaumometer`
Add a rotated crafting recipe for the Thaumometer.
