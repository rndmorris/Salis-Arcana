package dev.rndmorris.salisarcana.mixins.late.tiles;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode_DynamicHungryNodeReach extends TileThaumcraft {

    // todo: abstract this into a single shared field (or find some other way to cache it during a cycle) when other
    // node types get dynamic ranges
    @Unique
    private int sa$hungryRadius;

    @Shadow
    AspectList aspects;

    @Unique
    private double sa$calculateSizeMultiplier() {
        final var ratioBase = 4; // cube root of 64, which is roughly the average size of a normal-size node
        return Math.cbrt(this.aspects.visSize()) / ratioBase;
    }

    @Inject(method = "handleHungryNodeFirst", at = @At(value = "HEAD"))
    private void calculateHungryRadius(CallbackInfoReturnable<Boolean> cir) {
        sa$hungryRadius = (int) (16 * sa$calculateSizeMultiplier());
    }

    @WrapOperation(
        method = "handleHungryNodeFirst",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"),
        slice = @Slice(
            to = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getHeightValue(II)I", remap = true)))
    private int adjustParticleRandomCoordinate(Random instance, int i, Operation<Integer> original) {
        return original.call(instance, sa$hungryRadius);
    }

    @ModifyConstant(
        method = { "handleHungryNodeFirst", "handleHungryNodeSecond" },
        constant = @Constant(doubleValue = 256.0D))
    private double adjustMopDistance(double constant) {
        return sa$hungryRadius * sa$hungryRadius;
    }

    @ModifyConstant(method = "handleHungryNodeFirst", constant = @Constant(doubleValue = 15.0D))
    private double adjustEntityRange(double constant) {
        return constant * sa$calculateSizeMultiplier();
    }

    @WrapOperation(
        method = "handleHungryNodeSecond",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int adjustBlockRandomCoordinate(Random instance, int i, Operation<Integer> original) {
        return original.call(instance, sa$hungryRadius);
    }

    @ModifyConstant(method = "handleHungryNodeSecond", constant = @Constant(floatValue = 5.0F))
    private float adjustHardness(float constant) {
        return constant * (float) sa$calculateSizeMultiplier();
    }
}
