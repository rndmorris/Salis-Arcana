## Communication
// todo: preferences for feature requests and bug reports
// todo: avenues for dev discussion

## Salis Arcana's Goals

### Find and correct bugs
No code is perfect, and Thaumcraft 4 is no exception. Sometimes errors occur that can crash the game, log or announce error messages, or otherwise cause things in-game to behave in ways one wouldn't expect. Salis Arcana wants to fix those errors.

**Author's note:** Something behaving *"in ways one wouldn't expect"* can be very subjective. Is it *intentional* that banners, when being patterned with an essentia phial, consume both the essentia and the entire glass phial? Some would argue that it's clearly a bug because it doesn't make sense. Others would argue that, since there's no evidence that an effort was made in-code to *not* consume the whole phial, it's intended behavior. In these situations, it's best to discuss and try to find some agreement. If it's ultimately unclear, perhaps it would be better resolved as an additional configuration option.

### Add additional configuration options (aka "tweaks")
Everyone has different tastes an opinions. Additional configuration options are adjustable changes to things that are already part of Thaumcraft that ***do not*** involve adding new blocks, items, world generation, game mechanics, or anything that could be broadly classified as a "new feature". Tweaks that have the potential to significantly alter the balance of the mod should default to vanilla Thaumcraft's behavior.

**Author's note:** At the time of writing, a number of tweaks are categorized in Salis Arcana's config files as "enhancements" or "new features". These were added before this goal was established as its own initiative, and are currently left in place for backwards-compatability with user configurations from previous versions of the mod. New tweaks should be categorized appropriately.

### Add quality of life additions / new features
Sometimes we have "that would be convenient" ideas that aren't significant enough to be their own mod. Upgrading a wand's caps and core instead of crafting a wholly new wand, removing focus upgrades and getting some XP back in return, and scanning the contents of an entire chest at once are just some examples of what has been already added.

**Caveat:** As a firm rule, new items, blocks or anything else that changes a save file in a way that would violate the below goal of "Be as easy to uninstall as possible" will not be accepted. There are a few exceptions for legacy reasons, but any new exceptions will require significant justification.

### Be as easy to uninstall as possible
As the primary focus of Salis Arcana is to fix bugs and make Thaumcraft more configurable, we want to leave as little trace as we can. If someone wants to uninstall Salis Arcana, it should be as easy as possible to do so. This means that, as a firm rule, new items, blocks or anything else that changes a save file in a way that would make it difficult to uninstall the mod will not be accepted. As mentioned above, there are a few legacy exceptions but new ones are discouraged.

That's not to say it's impossible to add new things. Generally speaking, new crafting recipes and mechanics that rely on already-existing blocks and items can be safely implemented without irrevocably impacting the save file, and NBT tags can be used to track additional data without longterm consequences.

### Increase compatability with other mods
Thaumcraft has been around for over a decade, which means there's already a rich ecosystem of other mods designed to be compatible with it, be they add-ons or standalone mods. Where reasonable, the mechanisms by which other mods can make use of what Salis Arcana has added to the game should be made available through the api package, and mods that are still being actively maintained should be encouraged to use these mechanisms.

Unfortunately a number of mods designed to be compatible with Thaumcraft are either closed-source, no longer being maintained and updated, or both. When Salis Arcana adds a new feature or configuration option that could or does interact with these other mods, it's ultimately up to us to try to play nice with other mods. This doesn't mean every mod in existence will be immediately compatible, but compatability with the big names should be at least considered. Additional compatibility can be requested by submitting an issue.

## Standards and Best Practices
//todo: Preferred approaches to doing things

## Pull Requests
// todo: What we want to see, how we want to see it (the PR template, testing info, PR titles, etc)
