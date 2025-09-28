package dev.rndmorris.salisarcana.mixins.late.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public class MixinTileNode_Hungry_Brightness {

    @Shadow
    private NodeModifier nodeModifier;

    /**
     * Adjust the rate at which the node will try to break blocks, based on its modifier.
     */
    @ModifyConstant(method = "handleHungryNodeSecond", constant = @Constant(intValue = 50))
    private int brightnessAdjustment(int constant) {
        if (this.nodeModifier == null) {
            return constant;
        }
        return switch (this.nodeModifier) {
            case BRIGHT -> (int) ((double) constant * .8D);
            case PALE -> (int) ((double) constant * 1.2D);
            case FADING -> (int) ((double) constant * 1.5D);
        };
    }
}
