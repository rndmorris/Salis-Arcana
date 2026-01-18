package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.ItemResource;

@Mixin(ItemResource.class)
public abstract class MixinItemResource_DecayChance {

    @WrapOperation(
        method = "onUpdate",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false))
    private int disableCreativeDecay(Random instance, int i, Operation<Integer> original) {
        final var bound = SalisConfig.features.taintedItemDecayChance.getValue();
        if (bound < 0) {
            return Integer.MAX_VALUE;
        }
        return original.call(instance, bound);
    }
}
