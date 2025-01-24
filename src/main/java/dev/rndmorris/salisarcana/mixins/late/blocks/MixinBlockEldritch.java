package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch {

    @WrapOperation(
        method = "onBlockActivated",
        at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;stackSize:I", opcode = Opcodes.PUTFIELD))
    private void onDecStackSize(ItemStack instance, int newValue, Operation<Integer> original,
        @Local(argsOnly = true) EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return;
        }
        original.call(instance, newValue);
    }
}
