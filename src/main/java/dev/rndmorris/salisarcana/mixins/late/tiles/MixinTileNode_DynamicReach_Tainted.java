package dev.rndmorris.salisarcana.mixins.late.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import dev.rndmorris.salisarcana.lib.DynamicNodeLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public class MixinTileNode_DynamicReach_Tainted extends TileThaumcraft {

    @Shadow
    AspectList aspects;
    @Shadow
    private NodeType nodeType;

    /**
     * At the first opportunity, after we know the node will do tainty activities, calculate and cache the node's size
     * multiplier.
     */
    @Inject(
        method = "handleTaintNode",
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;xCoord:I", remap = true, ordinal = 0))
    private void calculateSizeMultiplier(boolean change, CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef, @Share("reach") LocalIntRef reachRef) {
        reachRef.set(-1);
        sizeMultiplierRef.set(DynamicNodeLogic.calculateSizeMultiplier(this.aspects.visSize()));
    }

    /**
     * Adjust the bound within which the node will convert biomes.
     */
    @ModifyExpressionValue(method = "handleTaintNode", at = @At(value = "CONSTANT", args = "intValue=8"))
    private int adjustCoordsForBiome(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        return DynamicNodeLogic.useReachMemo(constant, sizeMultiplierRef, reachRef);
    }

    /**
     * Reset memoized reach to re-use in tendril placement.
     */
    @Inject(
        method = "handleTaintNode",
        at = @At(value = "FIELD", target = "Lthaumcraft/common/config/Config;hardNode:Z"))
    private void resetReach(boolean change, CallbackInfoReturnable<Boolean> cir, @Share("reach") LocalIntRef reachRef) {
        reachRef.set(-1);
    }

    /**
     * Adjust the bound within which the node will place tendrils.
     */
    @ModifyExpressionValue(method = "handleTaintNode", at = @At(value = "CONSTANT", args = "intValue=5"))
    private int adjustCoordsForTendrils(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        return DynamicNodeLogic.useReachMemo(constant, sizeMultiplierRef, reachRef);
    }
}
