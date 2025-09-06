package dev.rndmorris.salisarcana.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class SalisArcanaHooks {
    public static AxisAlignedBB createBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(
            (double) x + minX,
            (double) y + minY,
            (double) z + minZ,
            (double) x + maxX,
            (double) y + maxY,
            (double) z + maxZ);
    }

    public static void addBoundingBox(Block block, World worldIn, int x, int y, int z, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collider, AxisAlignedBB box) {
        if (box == null) {
            box = block.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
        }

        if (box != null && mask.intersectsWith(box)) {
            list.add(box);
        }
    }
}
