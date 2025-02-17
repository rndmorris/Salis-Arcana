package dev.rndmorris.salisarcana.lib.customresearch.pages;

import java.util.Map;

import net.minecraft.item.ItemStack;

import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigResearch;

public class ArcaneResearchPageEntry extends RecipeResearchPageEntry {

    public ArcaneResearchPageEntry() {
        super();
    }

    public ArcaneResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "arcane";
        this.number = index;
    }

    @Override
    public ResearchPage getPage() {
        ItemStack stack = item.getItemStack();
        for (Map.Entry<String, Object> entry : ConfigResearch.recipes.entrySet()) {
            if (entry.getValue() instanceof ShapedArcaneRecipe || entry.getValue() instanceof ShapelessArcaneRecipe) {
                IArcaneRecipe recipe = (IArcaneRecipe) entry.getValue();
                if (recipe.getRecipeOutput()
                    .isItemEqual(stack)) {
                    return new ResearchPage(recipe);
                }
            } else if (entry.getValue() instanceof ShapedArcaneRecipe[]
                || entry.getValue() instanceof ShapelessArcaneRecipe[]) {
                    IArcaneRecipe[] recipe = (IArcaneRecipe[]) entry.getValue();
                    for (IArcaneRecipe recipe_ : recipe) {
                        if (recipe_.getRecipeOutput()
                            .isItemEqual(stack)) {
                            return new ResearchPage(recipe);
                        }
                    }
                }
        }
        return null;
    }
}
