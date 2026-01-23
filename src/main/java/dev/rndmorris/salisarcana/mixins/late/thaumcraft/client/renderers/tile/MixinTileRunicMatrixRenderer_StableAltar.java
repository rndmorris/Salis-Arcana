package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.tile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import thaumcraft.client.renderers.tile.TileRunicMatrixRenderer;

@Mixin(value = TileRunicMatrixRenderer.class, remap = false)
public class MixinTileRunicMatrixRenderer_StableAltar {

    @ModifyVariable(method = "renderInfusionMatrix", name = "instability", at = @At("STORE"))
    private float limitInstability(float value) {
        return Math.max(1f, value);
    }
}
