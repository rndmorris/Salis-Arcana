package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.api.IVariableInfusionStabilizer;
import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.tiles.TilePedestal;

public class InfusionMatrixLogic {

    final static int rangeHorizontal = 12;
    final static int rangeUp = 5;
    final static int rangeDown = 10;
    final static int pedestalRange = 8;

    /**
     * Calculate a matrix's stability and symmetry at a given position. Does not require a matrix actually be at that
     * position.
     *
     * @param world The world to check.
     * @param x     The z coordinate of the matrix.
     * @param y     The y coordinate of the matrix.
     * @param z     The z coordinate of the matrix.
     * @return The symmetry and list of pedestals that would influence the matrix.
     */
    public static MatrixSurroundingsResult checkMatrixSurroundings(World world, int x, int y, int z) {
        final var matrix = new MatrixOrigin(world, x, y, z);
        final var result = new MatrixSurroundingsResult();

        var stabilizerModifier = 0;
        for (var dX = -rangeHorizontal; dX <= rangeHorizontal; ++dX) {
            for (var dZ = -rangeHorizontal; dZ <= rangeHorizontal; ++dZ) {
                var pedestalInColumn = false;

                for (var dY = -rangeUp; dY <= rangeDown; ++dY) {
                    if (dX == 0 && dZ == 0) {
                        continue;
                    }

                    if (!pedestalInColumn && checkForAndHandlePedestal(matrix, result, dX, dY, dZ)) {
                        pedestalInColumn = true;
                        continue;
                    }
                    stabilizerModifier += getStabilityModifierAt(matrix, dX, dY, dZ);
                }
            }
        }

        result.symmetry += stabilizerModifier / 100;

        return result;
    }

    public static boolean isStabilizer(World world, int x, int y, int z) {
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);

        final var additions = SalisConfig.features.stabilizerAdditions;
        if (additions.isEnabled() && additions.hasEntry(block, metadata)) {
            return true;
        }

        final var isRegularStabilizer = block == Blocks.skull
            || (block instanceof IInfusionStabiliser stabilizer && stabilizer.canStabaliseInfusion(world, x, y, z));

        if (!isRegularStabilizer) {
            return false;
        }

        final var exclusions = SalisConfig.features.stabilizerExclusions;
        return !(exclusions.isEnabled() && exclusions.hasEntry(block, metadata));
    }

    private static boolean checkForAndHandlePedestal(MatrixOrigin matrix, MatrixSurroundingsResult result, int dX,
        int dY, int dZ) {
        if (Math.abs(dX) > pedestalRange || Math.abs(dZ) > pedestalRange) {
            return false;
        }

        final var x = matrix.xCoord + dX;
        final var y = matrix.yCoord - dY;
        final var z = matrix.zCoord + dZ;
        final var world = matrix.world;

        if (!(world.getTileEntity(x, y, z) instanceof TilePedestal pedestal)) {
            return false;
        }
        result.pedestals.add(new ChunkCoordinates(x, y, z));
        result.symmetry += 2;

        final var itemsInPair = pedestal.getStackInSlot(0) != null;
        if (itemsInPair) {
            result.symmetry += 1;
        }

        final var twin = getTwinnedCoord(matrix, x, z);
        if ((world.getTileEntity(twin[0], y, twin[1]) instanceof TilePedestal twinPedestal)) {
            result.symmetry -= 2;
            if (itemsInPair && twinPedestal.getStackInSlot(0) != null) {
                result.symmetry -= 1;
            }
        }

        return true;
    }

    /**
     * Get the symmetry modifier for the block at the given relative coordinates.
     *
     * @param matrix The matrix to use as the center point
     * @return The stabilizer's symmetry modifier.
     */
    private static int getStabilityModifierAt(MatrixOrigin matrix, int dX, int dY, int dZ) {
        final var x = matrix.xCoord + dX;
        final var y = matrix.yCoord - dY;
        final var z = matrix.zCoord + dZ;
        final var world = matrix.world;

        if (!InfusionMatrixLogic.isStabilizer(world, x, y, z)) {
            return 0;
        }

        final var strength = strengthForBlock(world, x, y, z);
        var twin = getTwinnedCoord(matrix, x, z);

        return InfusionMatrixLogic.isStabilizer(world, twin[0], y, twin[1]) ? strength * -1 : strength;
    }

    /**
     * Get the stabilizer strength of a block
     */
    private static int strengthForBlock(World world, int x, int y, int z) {
        final var features = SalisConfig.features;

        // If we're not using the rewrite, use the default stabilizer strength
        // Should only be called by the symmetry-check command
        if (!features.stabilizerStrength.isEnabled()) {
            return features.stabilizerStrength.getDefaultValue();
        }

        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);

        // If we have an override, use the override's value (or default if no value specified)
        final var additionData = features.stabilizerAdditions.getData(block, metadata);
        if (additionData.containedKeys) {
            return additionData.data != null ? additionData.data : features.stabilizerStrength.getValue();
        }

        // If an addon has tapped into Salis Arcana's API
        if (block instanceof IVariableInfusionStabilizer stabilizer) {
            return stabilizer.getStabilizerStrength(world, x, y, z);
        }

        // Or just the default strength
        return features.stabilizerStrength.getValue();
    }

    private static int[] getTwinnedCoord(MatrixOrigin matrix, int x, int z) {
        return new int[] { matrix.xCoord + (matrix.xCoord - x), matrix.zCoord + (matrix.zCoord - z), };
    }

    public static class MatrixSurroundingsResult {

        /**
         * The pedestals in range of an infusion altar (excluding directly above or below the matrix).
         */
        public final ArrayList<ChunkCoordinates> pedestals = new ArrayList<>();

        /**
         * How symmetrical an infusion altar is. Lower values are good, higher values are bad.
         */
        public int symmetry = 0;
    }

    private static class MatrixOrigin {

        private final World world;
        private final int xCoord;
        private final int yCoord;
        private final int zCoord;

        public MatrixOrigin(World world, int xCoord, int yCoord, int zCoord) {
            this.world = world;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.zCoord = zCoord;
        }

        public World world() {
            return world;
        }

        public int xCoord() {
            return xCoord;
        }

        public int yCoord() {
            return yCoord;
        }

        public int zCoord() {
            return zCoord;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MatrixOrigin that)) return false;
            return xCoord == that.xCoord && yCoord == that.yCoord
                && zCoord == that.zCoord
                && Objects.equals(world, that.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(world, xCoord, yCoord, zCoord);
        }
    }
}
