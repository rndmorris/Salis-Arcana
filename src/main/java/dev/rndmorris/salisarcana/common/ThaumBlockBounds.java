package dev.rndmorris.salisarcana.common;

import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.config.ConfigBlocks;

public class ThaumBlockBounds {

    public static void setCorrectBlockBounds() {
        ConfigBlocks.blockLootUrn.setBlockBounds(
            BlockRenderer.W2,
            BlockRenderer.W1,
            BlockRenderer.W2,
            BlockRenderer.W14,
            BlockRenderer.W13,
            BlockRenderer.W14);

        ConfigBlocks.blockLootCrate.setBlockBounds(
            BlockRenderer.W1,
            0.0F,
            BlockRenderer.W1,
            BlockRenderer.W15,
            BlockRenderer.W14,
            BlockRenderer.W15);

        ConfigBlocks.blockEssentiaReservoir.setBlockBounds(
            BlockRenderer.W2,
            BlockRenderer.W2,
            BlockRenderer.W2,
            BlockRenderer.W14,
            BlockRenderer.W14,
            BlockRenderer.W14);
    }
}
