# Enhancements

## Additional Bauble Slot Support

**Config option:** `useAllBaublesSlots`

All bauble slots, including those added by mods that extend Baubles' API, will be checked when
calculating vis discounts, runic armor, and focus pouches containing wand foci.

## Configurable Biome Colors
Biomes module

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


