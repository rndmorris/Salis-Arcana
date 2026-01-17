
package dev.rndmorris.salisarcana.mixins.late.automagy.config;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.api.IResearchItemExtended;
import thaumcraft.api.research.ResearchItem;
import tuhljin.automagy.config.ModResearchItem;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = ModResearchItem.class, remap = false)
public abstract class ModResearchItem_Extended extends ResearchItem implements IResearchItemExtended {

    public ModResearchItem_Extended(String key, String category) {
        super(key, category);
    }

    @Override
    public String getNameTranslationKey() {
        return String.format("Automagy.research_name.%s", this.key);
    }

    @Override
    public String getTextTranslationKey() {
        return String.format("Automagy.research_text.%s", this.key);
    }
}
