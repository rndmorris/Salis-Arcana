package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.blocks.BlockMetalDevice;

@Mixin(BlockMetalDevice.class)
public class MixinBlockMetalDevice_CreativePreserveWater {

    @WrapWithCondition(
        method = "onBlockActivated",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/InventoryPlayer;decrStackSize(II)Lnet/minecraft/item/ItemStack;"))
    private boolean preventWaterConsumption(InventoryPlayer instance, int itemstack, int i,
        @Local(argsOnly = true) EntityPlayer player) {
        return !player.capabilities.isCreativeMode;
    }

    @ModifyVariable(method = "onBlockActivated", at = @At("STORE"), name = "emptyContainer")
    private ItemStack noEmptyContainers(ItemStack value, @Local(argsOnly = true) EntityPlayer player) {
        return player.capabilities.isCreativeMode ? null : value;
    }
}
