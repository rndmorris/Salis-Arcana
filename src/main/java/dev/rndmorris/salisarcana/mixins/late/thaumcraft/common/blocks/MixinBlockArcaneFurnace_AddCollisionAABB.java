package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.blocks.BlockArcaneFurnace;

@Mixin(BlockArcaneFurnace.class)
abstract class MixinBlockArcaneFurnace_AddCollisionAABB extends BlockContainer {

    protected MixinBlockArcaneFurnace_AddCollisionAABB(Material materialIn) {
        super(materialIn);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        int meta = worldIn.getBlockMetadata(x, y, z);

        double minX = 0, minY = 0, minZ = 0, maxX = 1, maxY = 1, maxZ = 1;

        if (meta == 0) {
            maxY = 0.25;
        } else if (meta == 10) {
            if (worldIn.getBlockMetadata(x - 1, y, z) == 0) {
                maxX = 0.5;
            } else if (worldIn.getBlockMetadata(x + 1, y, z) == 0) {
                minX = 0.5;
            } else if (worldIn.getBlockMetadata(x, y, z - 1) == 0) {
                maxZ = 0.5;
            } else {
                minZ = 0.5;
            }
        }

        return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }
}
