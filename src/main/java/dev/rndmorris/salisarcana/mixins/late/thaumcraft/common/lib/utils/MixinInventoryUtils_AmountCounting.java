package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.utils;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.utils.InventoryUtils;

@Mixin(value = InventoryUtils.class, remap = false)
public class MixinInventoryUtils_AmountCounting {

    @Unique
    private static final ItemStack sa$nonNullItemStack = new ItemStack(Items.stick);

    @WrapOperation(
        method = "insertStack",
        at = { @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/utils/InventoryUtils;attemptInsertion(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/item/ItemStack;IIZ)Lnet/minecraft/item/ItemStack;",
            ordinal = 1),
            @At(
                value = "INVOKE",
                target = "Lthaumcraft/common/lib/utils/InventoryUtils;attemptInsertion(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/item/ItemStack;IIZ)Lnet/minecraft/item/ItemStack;",
                ordinal = 4) })
    private static ItemStack checkForEmpty(IInventory inventory, ItemStack stack, int slot, int side, boolean doit,
        Operation<ItemStack> original) {
        if (inventory.getStackInSlot(slot) == null) {
            return original.call(inventory, stack, slot, side, doit);
        } else {
            return stack;
        }
    }

    @WrapOperation(
        method = "insertStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/tileentity/TileEntityChest;getStackInSlot(I)Lnet/minecraft/item/ItemStack;",
            ordinal = 2,
            remap = true))
    private static ItemStack forceConditional(TileEntityChest instance, int slotIn, Operation<ItemStack> original) {
        return instance.getStackInSlot(slotIn) == null ? sa$nonNullItemStack : null;
    }

    @WrapOperation(
        method = "insertStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemEqual(Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 3,
            remap = true))
    private static boolean forceTruthy(ItemStack instance, ItemStack p_77969_1_, Operation<Boolean> original) {
        return true;
    }
}
