# Thaumcraft Configuration

Config file: `config/salisarcana/thaumcraft_configuration.cfg`

## Node Behaviors (Group `node_behaviors`)

### Config Option: `hungryDynamicReach` (Default `false`)
If true, the radius within which hungry nodes can break blocks and attract entities will scale with the current amount
of vis of the node.

### Config Option: `hungryModifierSpeed` (Default `true`)
If true, the rate at which hungry nodes will try to break blocks will be adjusted by their modifier: 20% more often when
bright, 20% less often when pale, 50% less often when fading.

### Config Option: `pureDynamicReach` (Default `false`)
If true, the radius within which pure nodes will convert their surroundings to Magical Forest will scale with the
current amount of vis of the node. Small pure nodes that are embedded in silverwood logs will have roughly the same
range as with this setting disabled.

### Config Option: `pureModifierSpeed` (Default `true`)
If true, the rate at which pure nodes will convert their surroundings to Magical Forest will be adjusted by their
modifier: 20% more often when bright, 20% less often when pale, 50% less often when fading.

### Config Option: `sinisterDynamicReach` (Default `false`)
If true, the radius within which sinister nodes will convert their surroundings to the Eerie biome will scale with the
current amount of vis of the node.

### Config Option: `sinisterModifierSpeed` (Default `true`)
If true, the rate at which sinister nodes will convert their surroundings to the Eerie biome and attempt to spawn
furious zombies will be adjusted by their modifier: 20% more often when bright, 20% less often when pale, 50% less often
when fading.

### Config Option: `taintedDynamicReach` (Default `false`)
If true, the radius within which tainted nodes will convert their surroundings to Tainted Lands and spawn taint tendrils
will scale with the current amount of vis of the node.

### Config Option: `taintedModifierSpeed` (Default `true`)
If true, the rate at which tainted nodes will convert their surroundings to Tainted Lands and spawn taint tendrils will
be adjusted by their modifier: 20% more often when bright, 20% less often when pale, 50% less often when fading.

## Pickaxe of the Core Detection Ore Dictionary Labels (Group `tools`)

### Config Option: `elementalPickOredictFilter`
When enabled, the Pickaxe of the Core will respect two new ore dictionary tags. Blocks labeled with
`salisarcana:elementalPickScanExclude` will never be detected by the pickaxe, while blocks labeled with
`salisarcana:elementalPickScanInclude` will be detected even if they normally would not.

## Primal Arrows (Group `primal_arrows`)

### Config Option: `canBeFiredFromDispensers`
Allows dispensers to fire primal arrows.

## Potion ID Overrides (Group `potion_ids`)

### Config Option: `_uncapped_potion_ids`
If true, will allow setting potion ids to 128 and higher. Defaults to `false` for safety.

### Config Option: `blurredVisionId`
Override the id of the Blurred Vision potion effect. Set to -1 to auto-assign as normal.

### Config Option: `deadlyGazeId`
Override the id of the Deadly Gaze potion effect. Set to -1 to auto-assign as normal.

### Config Option: `fluxFluId`
Override the id of the Flux Flu potion effect. Set to -1 to auto-assign as normal.

### Config Option: `fluxPhageId`
Override the id of the Flux Phage potion effect. Set to -1 to auto-assign as normal.

### Config Option: `sunScornedId`
Override the id of the Sun Scorned potion effect. Set to -1 to auto-assign as normal.

### Config Option: `taintPoisonId`
Override the id of the Taint Poison potion effect. Set to -1 to auto-assign as normal.

### Config Option: `thaumarhiaId`
Override the id of the Thaumarhia potion effect. Set to -1 to auto-assign as normal.

### Config Option: `unnaturalHungerId`
Override the id of the Unnatural Hunger potion effect. Set to -1 to auto-assign as normal.

### Config Option: `warpWardId`
Override the id of the Warp Ward potion effect. Set to -1 to auto-assign as normal.
