package dev.rndmorris.salisarcana.lib;

import org.jetbrains.annotations.Nullable;

import thaumcraft.api.nodes.NodeModifier;

public final class DynamicNodeLogic {

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
     * The cube root of {@link DynamicNodeLogic#NODE_SIZE_AVERAGE}.
     */
    public static final double NODE_SIZE_AVERAGE_CBRT = Math.cbrt(NODE_SIZE_AVERAGE);

    /**
     * The cube root of {@link DynamicNodeLogic#NODE_SIZE_AVERAGE_SMALL}.
     */
    public static final double NODE_SIZE_AVERAGE_SMALL_CBRT = Math.cbrt(NODE_SIZE_AVERAGE_SMALL);

    /**
     * A precomputed cache for a range of potential node sizes. Possibly a premature optimization, but one that should
     * be harmless enough.
     */
    private static final double[] cbrtMemo = ArrayHelper.calculateArray(new double[512], Math::cbrt);

    public static int brightnessSpeedAdjustment(int value, @Nullable NodeModifier modifier) {
        if (modifier == null) {
            return value;
        }

        if (value == 50) {
            return switch (modifier) {
                case BRIGHT -> 40;
                case PALE -> 60;
                case FADING -> 100;
                // just on the off chance someone hacked on a new modifier value
                // noinspection UnnecessaryDefault
                default -> value;
            };
        }

        return switch (modifier) {
            case BRIGHT -> (int) ((double) value * .8D);
            case PALE -> (int) ((double) value * 1.2D);
            case FADING -> (int) ((double) value * 1.5D);
            // just on the off chance someone hacked on a new modifier value
            // noinspection UnnecessaryDefault
            default -> value;
        };
    }

    /**
     * Calculate a node's multiplier based on its size. As node's size increases, so does its multiplier.
     *
     * @param visSize The total amount of vis currently in the node
     * @return The calculated multiplier.
     */
    public static double calculateSizeMultiplier(int visSize) {
        return (0 <= visSize && visSize < cbrtMemo.length ? cbrtMemo[visSize] : Math.cbrt(visSize))
            / DynamicNodeLogic.NODE_SIZE_AVERAGE_CBRT;
    }

    /**
     * Calculate a node's multiplier based on its size. As node's size increases, so does its multiplier.
     * Intended only for sufficiently-small pure nodes embedded in silverwood logs.
     *
     * @param visSize The total amount of vis currently in the node
     * @return The calculated multiplier.
     */
    public static double calculateSmallSizeMultiplier(int visSize) {
        return (0 <= visSize && visSize < cbrtMemo.length ? cbrtMemo[visSize] : Math.cbrt(visSize))
            / DynamicNodeLogic.NODE_SIZE_AVERAGE_SMALL_CBRT;
    }
}
