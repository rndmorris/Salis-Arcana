package dev.rndmorris.salisarcana.lib;

import net.glease.tc4tweak.modules.findRecipes.FindRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import cpw.mods.fml.common.Loader;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;

public class CraftingHelper {

    public static final CraftingHelper INSTANCE;

    static {
        if (Loader.isModLoaded("tc4tweak")) {
            INSTANCE = new TC4TweaksHelper();
        } else {
            INSTANCE = new CraftingHelper();
        }
    }

    private CraftingHelper() {}

    public IArcaneRecipe findArcaneRecipe(final IInventory awb, final EntityPlayer player) {
        final var recipes = ThaumcraftApi.getCraftingRecipes();

        for (final var recipe : recipes) {
            if (recipe instanceof IArcaneRecipe arcaneRecipe && arcaneRecipe.matches(awb, player.worldObj, player)) {
                return arcaneRecipe;
            }
        }

        return null;
    }

    private static class TC4TweaksHelper extends CraftingHelper {

        private TC4TweaksHelper() {}

        @Override
        public IArcaneRecipe findArcaneRecipe(final IInventory awb, final EntityPlayer player) {
            return FindRecipes.findArcaneRecipe(awb, player);
        }
    }
}
