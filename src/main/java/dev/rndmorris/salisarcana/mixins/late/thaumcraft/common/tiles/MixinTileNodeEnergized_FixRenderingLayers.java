package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.common.tiles.TileNodeEnergized;

@Mixin(TileNodeEnergized.class)
abstract class MixinTileNodeEnergized_FixRenderingLayers extends TileVisNode {

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
}
