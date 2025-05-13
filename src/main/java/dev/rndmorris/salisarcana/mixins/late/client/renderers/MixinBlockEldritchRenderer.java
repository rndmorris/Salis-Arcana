package dev.rndmorris.salisarcana.mixins.late.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.client.renderers.block.BlockEldritchRenderer;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.config.ConfigBlocks;

@Mixin(BlockEldritchRenderer.class)
public class MixinBlockEldritchRenderer {
    @Inject(method = "renderInventoryBlock", at = @At("HEAD"), remap = false)
    public void renderSolidBlocks(Block block, int metadata, int modelID, RenderBlocks renderer, CallbackInfo ci) {
        final IIcon[] icons = ((BlockEldritch) ConfigBlocks.blockEldritch).insIcon;

        if(metadata == 7) {
            renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
            BlockRenderer.drawFaces(renderer, block, icons[4], true);
        } else if (metadata == 10) {
            renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
            BlockRenderer.drawFaces(renderer, block, icons[5], icons[7], icons[6], icons[5], icons[6], icons[8], true);
        }
    }
}
