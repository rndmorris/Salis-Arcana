package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.renderers.block.BlockLootCrateRenderer;
import thaumcraft.client.renderers.block.BlockLootUrnRenderer;

@Mixin({ BlockLootUrnRenderer.class, BlockLootCrateRenderer.class })
public class MixinBlockLootRenderer_ConserveBlockBounds {

    @WrapOperation(
        method = "renderWorldBlock",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;setBlockBounds(FFFFFF)V"))
    public void setRenderBounds(Block instance, float minX, float minY, float minZ, float maxX, float maxY, float maxZ,
        Operation<Void> original, @Local(argsOnly = true) RenderBlocks renderer) {
        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @WrapOperation(
        method = "renderWorldBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderBlocks;setRenderBoundsFromBlock(Lnet/minecraft/block/Block;)V"))
    public void ignoreBoundsFromBlock(RenderBlocks instance, Block p_147775_1_, Operation<Void> original) {
        // The previous method sets the render bounds. This method intentionally does nothing.
    }
}
