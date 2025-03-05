package dev.rndmorris.salisarcana.lib.customresearch.pages;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import dev.rndmorris.salisarcana.lib.customresearch.ItemEntry;
import thaumcraft.api.research.ResearchPage;

public class CraftingResearchPageEntry extends RecipeResearchPageEntry {

    public CraftingResearchPageEntry() {
        super();
    }

    public CraftingResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "crafting";
        this.number = index;
        this.item = new ItemEntry(page.recipeOutput);
    }

    @Override
    public ResearchPage getPage() {
        ItemStack stack = item.getItemStack();
        if (stack == null) {
            return null;
        }
        for (Object obj : CraftingManager.getInstance()
            .getRecipeList()) {
            if (obj instanceof ShapedRecipes || obj instanceof ShapelessRecipes) {
                IRecipe recipe = (IRecipe) obj;
                if (recipe.getRecipeOutput()
                    .isItemEqual(stack)) {
                    return new ResearchPage(recipe);
                }
            } else if (obj instanceof ShapedRecipes[] || obj instanceof ShapelessRecipes[]) {
                IRecipe[] recipes = (IRecipe[]) obj;
                for (IRecipe recipe : recipes) {
                    if (recipe.getRecipeOutput()
                        .isItemEqual(stack)) {
                        return new ResearchPage(recipe);
                    }
                }
            }
        }
        return null;
    }
}
