package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

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
     * Adjust the volume within which a hungry node can draw particles.
     * Memoization is needed for {@code adjustMopDistance} below.
     * Wraps the first occurrence of a constant {@code 16} in each method, which should be part the very first
     * {@code rand.nextInt()} call.
     */
    @ModifyExpressionValue(
        method = { "handleHungryNodeFirst", "handleHungryNodeSecond" },
        at = @At(value = "CONSTANT", args = "intValue=16", ordinal = 0))
    private int adjustAndMemoReach(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        sizeMultiplierRef.set(DynamicNodeLogic.calculateSizeMultiplier(this.aspects.visSize()));
        final var reach = (int) (constant * sizeMultiplierRef.get());
        reachRef.set(reach);
        return reach;
    }

    /**
     * For {@code handleHungryNodeFirst}, adjust the volume within which a hungry node can draw particles.
     * For {@code handleHungryNodeSecond}, adjust the volume within which a hungry node can break blocks.
     * Targets from second occurrence of the constant {@code 16} to the first call to {@code world.getHeightValue()} to
     * keep the affected area scoped only to the desired {@code rand.nextInt()} calls.
     */
    @ModifyExpressionValue(
        method = { "handleHungryNodeFirst", "handleHungryNodeSecond" },
        at = @At(value = "CONSTANT", args = "intValue=16"),
        slice = @Slice(
            from = @At(value = "CONSTANT", args = "intValue=16", ordinal = 1),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;getHeightValue(II)I",
                remap = true,
                ordinal = 0)))
    private int adjustRandomCoords(int constant, @Share("reach") LocalIntRef reachRef) {
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
     * Adjust the maximum block hardness the node can break.
     */
    @ModifyExpressionValue(method = "handleHungryNodeSecond", at = @At(value = "CONSTANT", args = "floatValue=5.0"))
    private float adjustHardness(float constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return constant * (float) sizeMultiplierRef.get();
    }
}
