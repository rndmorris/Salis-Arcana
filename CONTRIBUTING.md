## Project Goals
* Be as easy to uninstall as possible
* Find and fix bugs
* Add configurability to base Thaumcraft
* Add quality of life (QoL) additions and minor features
* Increase compatibility with other mods

### Be as easy to uninstall as possible
If someone wants to uninstall Salis Arcana, it should be as easy as possible to do so. This means that,
as a firm rule, new items, blocks or anything else that changes a save file in a way that would make it difficult to
uninstall the mod will not be accepted. There are some legacy exceptions (notably the added greatwood and silverwood
plank blocks) but new exceptions will be granted only with exceedingly convincing justification.

New crafting recipes and mechanics that rely on already-existing blocks and items can generally be safely implemented
without irrevocably impacting the save file. NBT on tile entities and items can be used to track additional data
without long-term consequences.

### Increase compatibility with other mods
Thaumcraft has been around for over a decade, which means there's already a rich ecosystem of other mods designed to be
compatible with it, be they add-ons or standalone mods. Mechanisms by which other mods can use Salis Arcana's changes
and features should be made available through the api package where reasonable. Mods that are still being actively
maintained should be encouraged to use those mechanisms.

Compatibility with mods that are unmaintained should be added by Salis Arcana itself when needed.

## Change Categories
Everything in Salis Arcana is broadly grouped into a handful of categories.

### Bugfixes
This category includes changes that address anything that crashes the game, works in a way that seem contrary to
intended design, or outright doesn't work at all is a bug. If it's unclear if it's a bug or intended-but-unorthodox
design, the change might fit better as a tweak in the Thaumcraft category.

Changes in this category should be controlled by settings in `ConfigBugfixes` and be documented in `/docs/bugfixes.md`.

### Commands
This category consists solely of commands added by Salis Arcana. These commands can be for admins (e.g. `/create-node`
or `/upgrade-focus`) or standard players (e.g. `/infusion-symmetry` or `/prereqs`).

Commands should be controlled by settings in `ConfigCommands` and be documented in `/docs/commands.md`.

### Features
This category contains brand-new features that add new crafting recipes or add new functionality beyond simple tweaks
or adjustments.

Changes in this category should be controlled by settings in `ConfigFeatures` and be documented in
`/docs/enhancements.md`.

### Thaumcraft Tweaks
Changes that alter how Thaumcraft itself works, such as optimizations and behavior adjustments that wouldn't necessarily
be considered new features, belong in the Thaumcraft Tweaks category.

Changes in this category should be controlled by settings in `ConfigThaumcraft` and be documented in
`/docs/thaumcraft_configuration.md`.

### Mod Compatibility
Compatibility added between other mods and either Thaumcraft or Salis Arcana fall into this category.

Changes in this category should be controlled by settings in `ConfigModCompat` and be documented in
`/docs/mod-integrations.md`.

### Addons
These are changes that target unmaintained Thaumcraft-adjacent mods for reasons other than compatibility. At the time
of writing, this category contains a single bugfix for Automagy.

Changes in this category should be controlled by settings in `ConfigAddons` and be documented in `/docs/addons.md`.

## Standards and Best Practices

### Mixin classes should
* be package-private (no `public` or similar access modifier) and `abstract`.
* be named `Mixin<TargetClass>_<Suffix>`, where `<TargetClass>` is the name of the class being mixed into and `<Suffix>`
  is a short descriptor that describes the mixin's purpose and can be used to distinguish the mixin from others that
  target the same class.
  * If the mixin targets multiple classes, `<NameOfTargetClass>` may instead be a short descriptor of what kind of
    classes it targets. `_<Suffix>` is still required in this circumstance.
  * Some variation of the mixin's linked entry in `Mixins.java` can serve as a useful `<Suffix>`.
* be registered in `Mixins.java` under a descriptively-named enum value.
* use the prefix `salisarcana$` on any `@Unique` or non-mixin fields and methods.

### Mixin class members should
* be scoped as tightly as possible.
* play nicely with other mixins as best as possible.
  * Example: `@ModifyExpressionValue` should be preferred over `@Redirect` and `@ModifyConstant`.
* avoid using `@Overwrite` except when no other options are available.
* have appropriate `remap` values.
  * Use `remap = true` when targeting anything from vanilla Minecraft.
  * Use `remap = false` when targeting anything else.
  * It may sometimes be more convenient to make the top-level `@Mixin` annotation `remap = false` and selectively set
    `remap = true` only where needed. No need to specify `remap` when it would be redundant (`@Mixin(..., remap = true)`, for example).

### Pull Requests should
* adhere to the provided template.
* have a title that is written in the imperative voice and can serve as a short but descriptive entry in the changelog.
  * :x: "Fixes a confabulation machine bug"
  * :white_check_mark: "Prevent a null reference exception in confabulation machines"
* have a completed checklist.
