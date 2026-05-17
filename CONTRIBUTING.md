## Salis Arcana's Goals
* Be as easy to uninstall as possible
* Find and correct bugs
* Add configurability to base Thaumcraft
* Add Quality of Life (QoL) additions and minor features
* Increase compatibility with other mods

### Be as easy to uninstall as possible
If someone wants to uninstall Salis Arcana, it should be as easy as possible to do so. This means that,
as a firm rule, new items, blocks or anything else that changes a save file in a way that would make it difficult to
uninstall the mod will not be accepted. There are some legacy exceptions (notably the added greatwood and silverwood
plank blocks) but new exceptions will be granted only with exceedingly convincing justification.

New crafting recipes and mechanics that rely on already-existing blocks and items can generally be safely implemented
without irrevocably impacting the save file. NBT on tile entities and items can be used to track additional data
without long-term consequences.

### Increase compatability with other mods
Thaumcraft has been around for over a decade, which means there's already a rich ecosystem of other mods designed to be
compatible with it, be they add-ons or standalone mods. Mechanisms by which other mods can use Salis Arcana's changes
and features should be made available through the api package where reasonable. Mods that are still being actively
maintained should be encouraged to use these mechanisms.

Compatibility with mods that are closed-source and/or unmaintained should be added by Salis Arcana itself when needed.

## Standards and Best Practices

### Mixin classes should
* be `abstract`.
* be named `Mixin<TargetClass>_<Suffix>`, where `<TargetClass>` is the name of the class being mixed into and `<Suffix>`
  is a short descriptor that describes the mixin's purpose and can be used to distinguish the mixin from others that
  target the same class.
  * If the mixin targets multiple classes, `<NameOfTargetClass>` may instead be a short descriptor of what kind of
    classes it targets. `_<Suffix>` is still required in this circumstance.
  * Some variation of the mixin's linked entry in `Mixins.java` can serve as a useful `<Suffix>`.
* be registered in `Mixins.java` under a descriptively-named enum value.

### Pull Requests should
* adhere to the provided template.
* have a title that is written in the imperative voice and can serve as a short but descriptive entry in the changelog.
  * :x: "Fixes a confabulation machine bug"
  * :white_check_mark: "Prevent a null reference exception in confabulation machines"
* have a completed checklist.
