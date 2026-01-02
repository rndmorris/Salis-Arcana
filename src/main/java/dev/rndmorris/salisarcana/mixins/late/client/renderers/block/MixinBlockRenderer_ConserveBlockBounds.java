package dev.rndmorris.salisarcana.mixins.late.client.renderers.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.renderers.block.BlockArcaneFurnaceRenderer;
import thaumcraft.client.renderers.block.BlockCandleRenderer;
import thaumcraft.client.renderers.block.BlockCosmeticOpaqueRenderer;
import thaumcraft.client.renderers.block.BlockCustomOreRenderer;
import thaumcraft.client.renderers.block.BlockEldritchRenderer;
import thaumcraft.client.renderers.block.BlockEssentiaReservoirRenderer;
import thaumcraft.client.renderers.block.BlockGasRenderer;
import thaumcraft.client.renderers.block.BlockJarRenderer;
import thaumcraft.client.renderers.block.BlockLifterRenderer;
import thaumcraft.client.renderers.block.BlockLootCrateRenderer;
import thaumcraft.client.renderers.block.BlockLootUrnRenderer;
import thaumcraft.client.renderers.block.BlockMetalDeviceRenderer;
import thaumcraft.client.renderers.block.BlockStoneDeviceRenderer;
import thaumcraft.client.renderers.block.BlockTaintFibreRenderer;
import thaumcraft.client.renderers.block.BlockTaintRenderer;
import thaumcraft.client.renderers.block.BlockTubeRenderer;
import thaumcraft.client.renderers.block.BlockWoodenDeviceRenderer;

@Mixin({ BlockArcaneFurnaceRenderer.class, BlockCandleRenderer.class, BlockCosmeticOpaqueRenderer.class,
    BlockCustomOreRenderer.class, BlockEldritchRenderer.class, BlockEssentiaReservoirRenderer.class,
    BlockGasRenderer.class, BlockJarRenderer.class, BlockLifterRenderer.class, BlockLootCrateRenderer.class,
    BlockLootUrnRenderer.class, BlockMetalDeviceRenderer.class, BlockStoneDeviceRenderer.class,
    BlockTaintFibreRenderer.class, BlockTaintRenderer.class, BlockTubeRenderer.class, BlockWoodenDeviceRenderer.class })
public abstract class MixinBlockRenderer_ConserveBlockBounds {

    @Redirect(
        method = { "renderWorldBlock", "renderInventoryBlock" },
        at = @At(value = "INVOKE", target = "*(FFFFFF)V"),
        remap = false)
    private void setRenderBounds(@Coerce Block instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, @Local(argsOnly = true) RenderBlocks renderer) {
        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Redirect(
        method = { "renderWorldBlock", "renderInventoryBlock" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/RenderBlocks;setRenderBoundsFromBlock(Lnet/minecraft/block/Block;)V"))
    private void ignoreBoundsFromBlock(RenderBlocks instance, Block p_147775_1_) {
        // The previous method sets the render bounds. This method intentionally does nothing.
    }
}
