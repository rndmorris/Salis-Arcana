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
    public void registerFakeShapedArcaneRecipe(ShapedArcaneRecipe recipe) {
        if (recipe == null) {
            return;
        }
        shapedRecipes.add(recipe);
    }

    @Override
    public void registerFakeShapedArcaneRecipe(String research, ItemStack result, AspectList aspects,
        Object... recipe) {
        registerFakeShapedArcaneRecipe(new ShapedArcaneRecipe(research, result, aspects, recipe));
    }

    @Override
    public void registerFakeShapelessArcaneRecipe(ShapelessArcaneRecipe recipe) {
        if (recipe == null) {
            return;
        }
        shapelessArcaneRecipes.add(recipe);
    }

    @Override
    public void registerFakeShapelessArcaneRecipe(String research, ItemStack result, AspectList aspects,
        Object... recipe) {
        registerFakeShapelessArcaneRecipe(new ShapelessArcaneRecipe(research, result, aspects, recipe));
    }

    @Override
    public void registerFakeCrucibleRecipeHandler(CrucibleRecipe recipe) {
        if (recipe == null) {
            return;
        }
        crucibleRecipes.add(recipe);
    }

    @Override
    public void registerFakeCrucibleRecipeHandler(String key, ItemStack result, Object catalyst, AspectList tags) {
        registerFakeCrucibleRecipeHandler(new CrucibleRecipe(key, result, catalyst, tags));
    }

    @Override
    public void registerFakeInfusionRecipeHandler(InfusionRecipe recipe) {
        if (recipe == null) {
            return;
        }
        infusionRecipes.add(recipe);
    }

    @Override
    public void registerFakeInfusionRecipeHandler(String research, Object result, int instability, AspectList aspects,
        ItemStack input, ItemStack[] recipe) {
        registerFakeInfusionRecipeHandler(new InfusionRecipe(research, result, instability, aspects, input, recipe));
    }
}
