package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.tiles.TilePedestal;

public class InfusionMatrixLogic {

    final static int rangeHorizontal = 12;
    final static int rangeUp = 5;
    final static int rangeDown = 10;
    final static int pedestalRange = 8;

    private static int defaultStrength;
    private static boolean loadedStrength = false;

    public static int strength() {
        final var strSetting = ConfigModuleRoot.enhancements.stabilizerStrength;
        if (!strSetting.isEnabled()) {
            return strSetting.getDefaultValue();
        }
        if (!loadedStrength) {
            defaultStrength = ConfigModuleRoot.enhancements.stabilizerStrength.getValue();
            loadedStrength = true;
        }
        return defaultStrength;
    }

    public static int penalty(int strength) {
        return (int) Math.ceil(((double) strength) / 2D);
    }

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

        result.symmetry += stabilizerModifier / 10;

        return result;
    }

    public static boolean isStabilizer(World world, int x, int y, int z) {
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);

        final var additions = ConfigModuleRoot.enhancements.stabilizerAdditions;
        if (additions.isEnabled() && additions.hasMatch(block, metadata)) {
            return true;
        }

        final var isRegularStabilizer = block == Blocks.skull
            || (block instanceof IInfusionStabiliser stabilizer && stabilizer.canStabaliseInfusion(world, x, y, z));

        if (!isRegularStabilizer) {
            return false;
        }

        final var exclusions = ConfigModuleRoot.enhancements.stabilizerExclusions;
        return !(exclusions.isEnabled() && exclusions.hasMatch(block, metadata));
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
     * @return The effective
     */
    private static int getStabilityModifierAt(MatrixOrigin matrix, int dX, int dY, int dZ) {
        final var x = matrix.xCoord + dX;
        final var y = matrix.yCoord - dY;
        final var z = matrix.zCoord + dZ;
        final var world = matrix.world;

        if (!InfusionMatrixLogic.isStabilizer(world, x, y, z)) {
            return 0;
        }

        var modifier = penalty(strength());

        var twin = getTwinnedCoord(matrix, x, z);
        if (InfusionMatrixLogic.isStabilizer(world, twin[0], y, twin[1])) {
            modifier = -strength();
        }

        return modifier;
    }

    private static int[] getTwinnedCoord(MatrixOrigin matrix, int x, int z) {
        return new int[] { matrix.xCoord + (matrix.xCoord - x), matrix.zCoord + (matrix.zCoord - z), };
    }

    public static class MatrixSurroundingsResult {

        public final ArrayList<ChunkCoordinates> pedestals = new ArrayList<>();
        public int symmetry = 0;
    }

    @Desugar
    private record MatrixOrigin(World world, int xCoord, int yCoord, int zCoord) {}
}
