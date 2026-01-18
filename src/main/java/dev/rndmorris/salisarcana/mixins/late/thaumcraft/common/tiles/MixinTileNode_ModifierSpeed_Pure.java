package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import dev.rndmorris.salisarcana.lib.DynamicNodeLogic;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public class MixinTileNode_ModifierSpeed_Pure {

    @Shadow
    private NodeModifier nodeModifier;

    /**
     * Adjust the rate at which the node will do its pure thing.
     */
    @ModifyExpressionValue(method = "handlePureNode", at = @At(value = "CONSTANT", args = "intValue=50"))
    private int brightnessAdjustment(int constant) {
        return DynamicNodeLogic.brightnessSpeedAdjustment(constant, nodeModifier);
    }
}
