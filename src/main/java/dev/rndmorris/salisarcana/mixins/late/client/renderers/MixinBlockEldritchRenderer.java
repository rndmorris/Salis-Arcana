package dev.rndmorris.salisarcana.mixins.late.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.client.renderers.block.BlockEldritchRenderer;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.config.ConfigBlocks;

@Mixin(BlockEldritchRenderer.class)
public class MixinBlockEldritchRenderer {

    @WrapMethod(method = "renderInventoryBlock", remap = false)
    public void renderSolidBlocks(Block block, int metadata, int modelID, RenderBlocks renderer,
        Operation<Void> original) {
        final IIcon[] icons = ((BlockEldritch) ConfigBlocks.blockEldritch).insIcon;

        if (metadata == 7) {
            renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
            BlockRenderer.drawFaces(renderer, block, icons[4], true);
        } else if (metadata == 10) {
            renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
            BlockRenderer.drawFaces(renderer, block, icons[5], icons[7], icons[6], icons[5], icons[6], icons[8], true);
        } else {
            original.call(block, metadata, modelID, renderer);
        }
    }
}
