package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.lib.Opcodes;
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
abstract class MixinTileNode_DynamicReach_Dark extends TileThaumcraft {

    @Shadow
    AspectList aspects;

    /**
     * At the first opportunity, just before we know the node will do dark activities, calculate and cache the node's
     * size multiplier.
     */
    @Inject(
        method = "handleDarkNode",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileNode;xCoord:I",
            opcode = Opcodes.GETFIELD,
            remap = true,
            ordinal = 0),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                opcode = Opcodes.GETFIELD,
                target = "Lthaumcraft/common/tiles/TileNode;count:I")),
        cancellable = true)
    private void calculateSizeMultiplier(boolean change, CallbackInfoReturnable<Boolean> cir,
        @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef) {
        final var visSize = this.aspects.visSize();
        if (visSize == 0) {
            cir.setReturnValue(change);
            return;
        }
        sizeMultiplierRef.set(DynamicNodeLogic.calculateSizeMultiplier(visSize));
    }

    /**
     * Adjust the bound within which the node will convert biomes and place tendrils
     */
    @ModifyExpressionValue(
        method = "handleDarkNode",
        at = @At(value = "CONSTANT", args = "intValue=12"),
        slice = @Slice(
            to = @At(
                value = "FIELD",
                opcode = Opcodes.GETSTATIC,
                target = "Lthaumcraft/common/config/Config;hardNode:Z")))
    private int adjustCoordsForBiome(int constant, @Share("sizeMultiplier") LocalDoubleRef sizeMultiplierRef,
        @Share("reach") LocalIntRef reachRef) {
        return (int) (constant * sizeMultiplierRef.get());
    }
}
