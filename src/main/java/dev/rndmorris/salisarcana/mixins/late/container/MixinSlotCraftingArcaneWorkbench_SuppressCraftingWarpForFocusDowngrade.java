package dev.rndmorris.salisarcana.mixins.late.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import cpw.mods.fml.common.FMLCommonHandler;
import dev.rndmorris.salisarcana.common.interfaces.ISuppressCraftingWarp;
import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.config.Config;
import thaumcraft.common.container.SlotCraftingArcaneWorkbench;

@Mixin(value = SlotCraftingArcaneWorkbench.class)
public abstract class MixinSlotCraftingArcaneWorkbench_SuppressCraftingWarpForFocusDowngrade extends SlotCrafting {

    public MixinSlotCraftingArcaneWorkbench_SuppressCraftingWarpForFocusDowngrade(EntityPlayer p_i1823_1_,
        IInventory p_i1823_2_, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
        super(p_i1823_1_, p_i1823_2_, p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
    }

    @WrapOperation(
        method = "onPickupFromSlot",
        at = @At(
            value = "INVOKE",
            target = "Lcpw/mods/fml/common/FMLCommonHandler;firePlayerCraftingEvent(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)V",
            remap = false))
    private void wrapFirePlayerCraftingEvent(FMLCommonHandler instance, EntityPlayer player, ItemStack itemStack,
        IInventory inventory, Operation<Void> original, @Local(argsOnly = true) ItemStack par1ItemStack) {
        if (Config.wuss || ThaumcraftApi.getWarp(par1ItemStack) <= 0) {
            // this only matters if the output would give the crafting player warp
            original.call(instance, player, itemStack, inventory);
            return;
        }

        IArcaneRecipe matchedRecipe = null;
        for (var recipe : ThaumcraftApi.getCraftingRecipes()) {
            if (recipe instanceof IArcaneRecipe arcaneRecipe
                && arcaneRecipe.matches(inventory, player.getEntityWorld(), player)) {
                matchedRecipe = arcaneRecipe;
                break;
            }
        }
        if (matchedRecipe == CustomRecipes.cleanFocusRecipe) {
            // this recipe shouldn't give warp
            final var extendedEventWorldHandler = ISuppressCraftingWarp.getInstance();
            if (extendedEventWorldHandler != null) {
                extendedEventWorldHandler.skipNextCraftingWarp();
            }
        }
        original.call(instance, player, itemStack, inventory);
    }

}
