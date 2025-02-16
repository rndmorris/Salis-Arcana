package dev.rndmorris.salisarcana.lib.customresearch.pages;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import thaumcraft.api.research.ResearchPage;

public class SmeltingResearchPageEntry extends RecipeResearchPageEntry {

    public SmeltingResearchPageEntry() {}

    public SmeltingResearchPageEntry(ResearchPage page, Integer index) {
        this.type = "smelting";
        this.number = index;
    }

    @Override
    public ResearchPage getPage() {
        ItemStack stack = item.getItemStack();
        if (stack == null) {
            return null;
        }
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            if (entry.getValue()
                .isItemEqual(stack)) {
                return new ResearchPage(entry.getKey());
            }
        }
        return null;
    }
}
