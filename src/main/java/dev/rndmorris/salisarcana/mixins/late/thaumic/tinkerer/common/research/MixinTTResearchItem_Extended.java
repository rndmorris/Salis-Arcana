package dev.rndmorris.salisarcana.mixins.late.thaumic.tinkerer.common.research;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import thaumcraft.api.research.ResearchItem;
import thaumic.tinkerer.common.research.TTResearchItem;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = TTResearchItem.class, remap = false)
public abstract class MixinTTResearchItem_Extended extends ResearchItem implements IResearchItemExtended {

    public MixinTTResearchItem_Extended(String key, String category) {
        super(key, category);
    }

    @Override
    public String getNameTranslationKey() {
        return String.format("ttresearch.name.%s", this.key);
    }

    @Override
    public String getTextTranslationKey() {
        return String.format("ttresearch.lore.%s", this.key);
    }
}
