package dev.rndmorris.salisarcana.lib;

import net.minecraft.block.Block;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.ConfigBlocks;

public final class NodeStatistics {

    /**
     * The average size of a normal-sized node at world generation. Sample size: 1mil+
     */
    public static final double NODE_SIZE_AVERAGE = 63;

    /**
     * The average size of a small-sized node at world generation. Sample size: 1mil+
     */
    public static final double NODE_SIZE_AVERAGE_SMALL = 13.5D;

    /**
     * The maximum size of a small-sized node at world generation. Sample size: 2mil+
     */
    public static final int NODE_SIZE_SMALL_MAX = 19;

    /**
     * The cube root of {@link NodeStatistics#NODE_SIZE_AVERAGE}.
     */
    public static final double NODE_SIZE_AVERAGE_CBRT = Math.cbrt(NODE_SIZE_AVERAGE);

    /**
     * The cube root of {@link NodeStatistics#NODE_SIZE_AVERAGE_SMALL}.
     */
    public static final double NODE_SIZE_AVERAGE_SMALL_CBRT = Math.cbrt(NODE_SIZE_AVERAGE_SMALL);

    /**
     * Calculate a node's multiplier based on its size. As node's size increases, so does its multiplier.
     * 
     * @param aspects   {@code TileNode#aspects}
     * @param nodeType  {@code TileNode#nodeType}
     * @param blockType {@code TileEntity#blockType}
     * @return The calculated multiplier.
     */
    public static double calculateSizeMultiplier(AspectList aspects, NodeType nodeType, Block blockType) {
        final var nodeSize = aspects.visSize();
        final var cbrtSize = Math.cbrt(nodeSize);
        var divisor = NodeStatistics.NODE_SIZE_AVERAGE_CBRT;

        // make a special exception for pure nodes in silverwood trees that are no larger
        // than the biggest small node. This lets silverwood trees stay roughly as strong as in vanilla TC.
        if (nodeType == NodeType.PURE && blockType == ConfigBlocks.blockMagicalLog
            && nodeSize <= NodeStatistics.NODE_SIZE_SMALL_MAX) {
            divisor = NodeStatistics.NODE_SIZE_AVERAGE_SMALL_CBRT;
        }

        return cbrtSize / divisor;
    }
}
