package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;

import dev.rndmorris.salisarcana.lib.DynamicNodeLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public class MixinTileNode_DynamicReach_Pure extends TileThaumcraft {

    @Shadow
    AspectList aspects;

    /**
     * At the first opportunity, after we know the node will do tainty activities, calculate and cache the node's size
     * multiplier.
     */
    @Inject(
        method = "handlePureNode",
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;xCoord:I", remap = true, ordinal = 0))
    private void calculateSizeMultiplier(boolean change, CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        final var visSize = this.aspects.visSize();
        if (this.getBlockType() == ConfigBlocks.blockMagicalLog && visSize <= DynamicNodeLogic.NODE_SIZE_SMALL_MAX) {
            // special exception for sufficiently-small in-log nodes
            sizeMultiplierRef.set(DynamicNodeLogic.calculateSmallSizeMultiplier(visSize));
            return;
        }
        sizeMultiplierRef.set(DynamicNodeLogic.calculateSizeMultiplier(visSize));
    }

    /**
     * Adjust the bound within which the node will convert biomes.
     */
    @ModifyExpressionValue(method = "handlePureNode", at = @At(value = "CONSTANT", args = "intValue=8"))
    private int adjustCoordsForBiome(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        return (int) (constant * sizeMultiplierRef.get());
    }
}
