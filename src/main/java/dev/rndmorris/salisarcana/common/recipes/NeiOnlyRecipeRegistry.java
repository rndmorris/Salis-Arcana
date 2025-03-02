package dev.rndmorris.salisarcana.common.recipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import dev.rndmorris.salisarcana.api.INeiOnlyRecipeRegistry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

public class NeiOnlyRecipeRegistry implements INeiOnlyRecipeRegistry {

    private static NeiOnlyRecipeRegistry instance;

    public static synchronized NeiOnlyRecipeRegistry getInstance() {
        if (instance == null) {
            instance = new NeiOnlyRecipeRegistry();
        }
        return instance;
    }

    public final ArrayList<ShapedArcaneRecipe> shapedRecipes = new ArrayList<>();
    public final ArrayList<ShapelessArcaneRecipe> shapelessArcaneRecipes = new ArrayList<>();
    public final ArrayList<CrucibleRecipe> crucibleRecipes = new ArrayList<>();
    public final ArrayList<InfusionRecipe> infusionRecipes = new ArrayList<>();

    @Override
    public ShapedArcaneRecipe registerFakeShapedArcaneRecipe(ShapedArcaneRecipe recipe) {
        if (recipe != null) {
            shapedRecipes.add(recipe);
        }
        return recipe;
    }

    @Override
    public ShapedArcaneRecipe registerFakeShapedArcaneRecipe(String research, ItemStack result, AspectList aspects,
        Object... recipe) {
        return registerFakeShapedArcaneRecipe(new ShapedArcaneRecipe(research, result, aspects, recipe));
    }

    @Override
    public ShapelessArcaneRecipe registerFakeShapelessArcaneRecipe(ShapelessArcaneRecipe recipe) {
        if (recipe != null) {
            shapelessArcaneRecipes.add(recipe);
        }
        return recipe;
    }

    @Override
    public ShapelessArcaneRecipe registerFakeShapelessArcaneRecipe(String research, ItemStack result,
        AspectList aspects, Object... recipe) {
        return registerFakeShapelessArcaneRecipe(new ShapelessArcaneRecipe(research, result, aspects, recipe));
    }

    @Override
    public CrucibleRecipe registerFakeCrucibleRecipeHandler(CrucibleRecipe recipe) {
        if (recipe != null) {
            crucibleRecipes.add(recipe);
        }
        return recipe;
    }

    @Override
    public CrucibleRecipe registerFakeCrucibleRecipeHandler(String key, ItemStack result, Object catalyst,
        AspectList tags) {
        return registerFakeCrucibleRecipeHandler(new CrucibleRecipe(key, result, catalyst, tags));
    }

    @Override
    public InfusionRecipe registerFakeInfusionRecipeHandler(InfusionRecipe recipe) {
        if (recipe != null) {
            infusionRecipes.add(recipe);
        }
        return recipe;
    }

    @Override
    public InfusionRecipe registerFakeInfusionRecipeHandler(String research, Object result, int instability,
        AspectList aspects, ItemStack input, ItemStack[] recipe) {
        return registerFakeInfusionRecipeHandler(
            new InfusionRecipe(research, result, instability, aspects, input, recipe));
    }
}
