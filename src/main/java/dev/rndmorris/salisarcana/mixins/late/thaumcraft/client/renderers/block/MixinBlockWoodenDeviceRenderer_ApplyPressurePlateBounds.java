package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.renderers.block.BlockWoodenDeviceRenderer;

@Mixin(value = BlockWoodenDeviceRenderer.class, priority = 1001)
abstract class MixinBlockWoodenDeviceRenderer_ApplyPressurePlateBounds {

    @Redirect(
        method = "renderWorldBlock",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;setBlockBounds(FFFFFF)V", ordinal = 11))
    private void applyRenderBounds(Block instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, @Local(argsOnly = true) RenderBlocks renderBlocks, @Local(name = "md") int md) {
        float height = md == 2 ? 0.0625f : 0.03125f;
        renderBlocks.setRenderBounds(0.0625f, 0f, 0.0625f, 0.9375f, height, 0.9375f);
    }
}
