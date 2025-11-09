package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.items.ItemResource;

@Mixin(ItemResource.class)
public abstract class MixinItemResource_DisableCreativeDecay {

    @WrapOperation(
        method = "onUpdate",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int disableCreativeDecay(Random instance, int i, Operation<Integer> original,
        @Local(argsOnly = true) Entity entity) {
        if (entity instanceof EntityPlayer player && player.capabilities.isCreativeMode) {
            return Integer.MAX_VALUE;
        }
        return original.call(instance, i);
    }
}
