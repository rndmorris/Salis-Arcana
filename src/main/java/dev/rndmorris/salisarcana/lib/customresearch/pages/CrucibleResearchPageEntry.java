package dev.rndmorris.salisarcana.lib.customresearch.pages;

import java.util.Map;

import net.minecraft.item.ItemStack;

import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigResearch;

public class CrucibleResearchPageEntry extends RecipeResearchPageEntry {

    public CrucibleResearchPageEntry() {}

    public CrucibleResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "crucible";
        this.number = index;
    }

    @Override
    public ResearchPage getPage() {
        ItemStack stack = item.getItemStack();
        for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
            if (entry.getValue() instanceof CrucibleRecipe recipe) {
                if (recipe.getRecipeOutput()
                    .isItemEqual(stack)) {
                    return new ResearchPage(entry.getKey());
                }
            }
        }
        return null;
    }
}
