package dev.rndmorris.salisarcana.mixins.late.tiles;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import dev.rndmorris.salisarcana.lib.NodeStatistics;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode_Hungry_DynamicReach extends TileThaumcraft {

    @Shadow
    AspectList aspects;
    @Shadow
    private NodeType nodeType;

    /**
     * At the start of {@code handleHungryNodeFirst} and {@code handleHungryNodeSecond}, calculate the node's hungry
     * reach
     */
    @Inject(method = { "handleHungryNodeFirst", "handleHungryNodeSecond" }, at = @At(value = "HEAD"))
    private void calculateHungryRadius(CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef, @Share("hungryReach") LocalIntRef hungryReachRef) {
        final var sizeMultiplier = NodeStatistics.calculateSizeMultiplier(this.aspects, this.nodeType, this.blockType);
        final var hungryReach = (int) Math
            .floor(16D * NodeStatistics.calculateSizeMultiplier(this.aspects, this.nodeType, this.blockType));
        sizeMultiplierRef.set(sizeMultiplier);
        hungryReachRef.set(hungryReach);
    }

    /**
     * Adjust the volume within which a hungry node can draw particles
     */
    @WrapOperation(
        method = "handleHungryNodeFirst",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"),
        slice = @Slice(
            to = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getHeightValue(II)I", remap = true)))
    private int adjustParticleRandomCoordinate(Random instance, int i, Operation<Integer> original,
        @Share("hungryReach") LocalIntRef hungryReachRef) {
        return original.call(instance, hungryReachRef.get());
    }

    /**
     * Adjust the maximum distance from which the node will draw particles and eat blocks.
     */
    @ModifyConstant(
        method = { "handleHungryNodeFirst", "handleHungryNodeSecond" },
        constant = @Constant(doubleValue = 256.0D))
    private double adjustMopDistance(double constant, @Share("hungryReach") LocalIntRef hungryReachRef) {
        final var hungryReach = hungryReachRef.get();
        return hungryReach * hungryReach;
    }

    /**
     * Adjust the bounding box used to select entities the node will drag towards it, and the divisors used to calculate
     * how much motion each entity receives.
     */
    @ModifyConstant(method = "handleHungryNodeFirst", constant = @Constant(doubleValue = 15.0D))
    private double adjustEntityBB(double constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return constant * sizeMultiplierRef.get();
    }

    /**
     * Adjust the volume within which a hungry node can eat blocks
     */
    @WrapOperation(
        method = "handleHungryNodeSecond",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int adjustBlockRandomCoordinate(Random instance, int i, Operation<Integer> original,
        @Share("hungryReach") LocalIntRef hungryReachRef) {
        return original.call(instance, hungryReachRef.get());
    }

    /**
     * Adjust the maximum block hardness the node can break.
     */
    @ModifyConstant(method = "handleHungryNodeSecond", constant = @Constant(floatValue = 5.0F))
    private float adjustHardness(float constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return constant * (float) sizeMultiplierRef.get();
    }
}
