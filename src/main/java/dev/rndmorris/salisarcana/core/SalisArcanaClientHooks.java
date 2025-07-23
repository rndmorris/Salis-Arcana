package dev.rndmorris.salisarcana.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

@SuppressWarnings("unused")
public class SalisArcanaClientHooks {
    public static void setRenderBoundsWithoutBlock(Block block, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, RenderBlocks renderer) {
        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
