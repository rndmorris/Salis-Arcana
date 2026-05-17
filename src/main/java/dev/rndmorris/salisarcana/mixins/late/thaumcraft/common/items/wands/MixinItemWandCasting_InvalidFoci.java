package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(ItemWandCasting.class)
public abstract class MixinItemWandCasting_InvalidFoci {

    @WrapOperation(
        method = "getFocus",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item preventInvalidFocus(ItemStack instance, Operation<Item> original) {
        if (instance == null) {
            return null;
        } else {
            Item item = original.call(instance);
            return item instanceof ItemFocusBasic ? item : null;
        }
    }

    @ModifyExpressionValue(
        method = "getFocusItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;loadItemStackFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack preventInvalidFocusStack(ItemStack original) {
        if (original != null && original.getItem() instanceof ItemFocusBasic) {
            return original;
        }
        return null;
    }
}
