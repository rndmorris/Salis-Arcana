package dev.rndmorris.salisarcana.lib.customresearch.pages;

import java.util.Map;

import net.minecraft.item.ItemStack;

import dev.rndmorris.salisarcana.lib.StringHelper;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigResearch;

public class InfusionResearchPageEntry extends RecipeResearchPageEntry {

    public InfusionResearchPageEntry() {}

    public InfusionResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "infusion";
        this.number = index;
    }

    @Override
    public ResearchPage getPage() {
        ItemStack stack = StringHelper.parseItemFromString(item.getItem());
        if (stack == null) {
            return null;
        }
        stack.stackSize = item.getAmount();
        stack.setItemDamage(item.getMeta());
        for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
            if (entry.getValue() instanceof InfusionRecipe recipe) {
                if (((ItemStack) recipe.getRecipeOutput()).isItemEqual(stack)) {
                    return new ResearchPage(entry.getKey());
                }
            }
        }
        return null;
    }
}
