package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileNode;

@Mixin(TileNode.class)
abstract class MixinTileNode_FixRenderingLayers extends TileThaumcraft {

    /**
     * With the default 'return pass == 0', the node will always render behind transparent blocks.
     * With 'return pass == 1', the node will render in front of transparent blocks.
     * Rendering in front is desired because 99% of the time, nodes are only seen with goggles of revealing which
     * make the nodes much more visible. When nodes are looked at directly, water or glass behind them should not be
     * rendering in front of them.
     */
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
}
