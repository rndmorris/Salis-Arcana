package dev.rndmorris.salisarcana.mixins.late.thaumcraft.api.research;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import thaumcraft.api.research.ResearchItem;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = ResearchItem.class, remap = false)
public abstract class ResearchItem_Extended implements IResearchItemExtended {

    @Shadow
    @Final
    public String key;

    @Override
    public String getNameTranslationKey() {
        return String.format("tc.research_name.%s", this.key);
    }

    @Override
    public String getTextTranslationKey() {
        return String.format("tc.research_text.%s", this.key);
    }
}
