package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.common.tiles.TileNodeEnergized;

@Mixin(TileNodeEnergized.class)
abstract class MixinTileNodeEnergized_FixRenderingLayers extends TileVisNode {

    /**
     * With the default 'return pass == 0', the node will always render behind transparent blocks.
     * With 'return pass == 1', the node will render in front of transparent blocks.
     * Rendering in front is desired because most of the time the node will be looked at directly,
     * and it is 'energized' so should be highly visible. When nodes are looked at directly, water or glass behind them
     * should not be rendering in front of them.
     */
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
}
