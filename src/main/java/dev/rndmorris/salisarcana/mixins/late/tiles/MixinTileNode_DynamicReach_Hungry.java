package dev.rndmorris.salisarcana.mixins.late.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import dev.rndmorris.salisarcana.lib.DynamicNodeLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode_DynamicReach_Hungry extends TileThaumcraft {

    @Shadow
    AspectList aspects;

    /**
     * Once we know that this is a hungry node in {@code handleHungryNodeFirst}, calculate the node's size multiplier.
     */
    @Inject(
        method = { "handleHungryNodeFirst" },
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z", remap = true))
    private void calculateSizeMultiplierFirst(CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        // we don't actually need to do anything with `isRemote`, it's just a convenient target
        final var sizeMultiplier = DynamicNodeLogic.calculateSizeMultiplier(this.aspects.visSize());
        sizeMultiplierRef.set(sizeMultiplier);
    }

    /**
     * Adjust the volume within which a hungry node can draw particles
     */
    @ModifyExpressionValue(
        method = "handleHungryNodeFirst",
        at = @At(value = "CONSTANT", args = "intValue=16"),
        slice = @Slice(
            to = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getHeightValue(II)I", remap = true)))
    private int adjustParticleCoords(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        if (reachRef.get() == 0) {
            final var value = (int) (constant * sizeMultiplierRef.get());
            reachRef.set(value > 0 ? value : 1);
        }
        return reachRef.get();
    }

    /**
     * Adjust the maximum distance from which the node will draw particles and eat blocks.
     */
    @ModifyExpressionValue(
        method = { "handleHungryNodeFirst", "handleHungryNodeSecond" },
        at = @At(value = "CONSTANT", args = "doubleValue=256.0D"))
    private double adjustMopDistance(double constant, @Share("reach") LocalIntRef reachRef) {
        // 256 is 16^2. Since we probably aren't using 16, we use the reach calculated earlier
        final var val = reachRef.get();
        return val * val;
    }

    /**
     * Adjust the bounding box used to select entities the node will drag towards it, and the divisors used to calculate
     * how much motion each entity receives.
     */
    @ModifyExpressionValue(method = "handleHungryNodeFirst", at = @At(value = "CONSTANT", args = "doubleValue=15.0"))
    private double adjustEntityBB(double constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return constant * sizeMultiplierRef.get();
    }

    /**
     * The first time we know we'll need the multiplier in {@code handleHungryNodeSecond}, calculate it.
     * No need to execute this every tick for every node if it'll only be needed once every 40/50/60/100 ticks.
     */
    @Inject(
        method = "handleHungryNodeSecond",
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;xCoord:I", remap = true, ordinal = 0))
    private void calculateSizeMultiplierSecond(CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        final var sizeMultiplier = DynamicNodeLogic.calculateSizeMultiplier(this.aspects.visSize());
        sizeMultiplierRef.set(sizeMultiplier);
    }

    /**
     * Adjust the volume within which a hungry node can eat blocks
     */
    @ModifyExpressionValue(
        method = "handleHungryNodeSecond",
        at = @At(value = "CONSTANT", args = "intValue=16"),
        slice = @Slice(
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;getHeightValue(II)I",
                remap = true,
                ordinal = 0)))
    private int adjustBlockRandomCoordinate(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        if (reachRef.get() == 0) {
            final var value = (int) (constant * sizeMultiplierRef.get());
            reachRef.set(value > 0 ? value : 1);
        }
        return reachRef.get();
    }

    /**
     * Adjust the maximum block hardness the node can break.
     */
    @ModifyExpressionValue(method = "handleHungryNodeSecond", at = @At(value = "CONSTANT", args = "floatValue=5.0"))
    private float adjustHardness(float constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return constant * (float) sizeMultiplierRef.get();
    }
}
