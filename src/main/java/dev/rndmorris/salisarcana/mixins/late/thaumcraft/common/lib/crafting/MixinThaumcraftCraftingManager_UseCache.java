package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.NullAspectList;
import dev.rndmorris.salisarcana.lib.ifaces.ICachedMagicWorkbench;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

@Mixin(ThaumcraftCraftingManager.class)
public class MixinThaumcraftCraftingManager_UseCache {

    @WrapMethod(method = "findMatchingArcaneRecipe", remap = false)
    private static ItemStack checkArcaneRecipeCache(IInventory awb, EntityPlayer player,
        Operation<ItemStack> original) {
        if (awb instanceof ICachedMagicWorkbench cache) {
            final IArcaneRecipe recipe = cache.salisArcana$getArcaneRecipe();

            if (recipe == null) return null;
            if (!recipe.matches(awb, player.worldObj, player)) return null;

            return recipe.getCraftingResult(awb);
        }

        return original.call(awb, player);
    }

    @WrapMethod(method = "findMatchingArcaneRecipeAspects", remap = false)
    private static AspectList checkArcaneRecipeCacheAspects(IInventory awb, EntityPlayer player,
        Operation<AspectList> original) {
        if (awb instanceof ICachedMagicWorkbench cache) {
            final IArcaneRecipe recipe = cache.salisArcana$getArcaneRecipe();

            if (recipe == null) return NullAspectList.INSTANCE;
            if (!recipe.matches(awb, player.worldObj, player)) return NullAspectList.INSTANCE;

            // This is stupid, but this is how the base method works.
            final AspectList aspectsSimple = recipe.getAspects();
            return aspectsSimple != null ? aspectsSimple : recipe.getAspects(awb);
        }

        return original.call(awb, player);
    }
}
