package dev.rndmorris.tfixins.mixins.late.blocks;

import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;

import com.midnight.metaawareblocks.api.IMetaAware;

import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin(BlockWoodenDevice.class)
public abstract class MixinBlockWoodenDevice implements IMetaAware {

    @Override
    public boolean renderAsNormalBlock(IBlockAccess world, int x, int y, int z) {
        // planks
        return world.getBlockMetadata(x, y, z) == 6 || world.getBlockMetadata(x, y, z) == 7;
    }

    @Override
    public boolean isOpaqueCube(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) == 6 || world.getBlockMetadata(x, y, z) == 7;
    }

    @Override
    public boolean canProvidePower(IBlockAccess world, int x, int y, int z) {
        // Arcane Ear, Arcane Pressure Plate
        return world.getBlockMetadata(x, y, z) == 1 || world.getBlockMetadata(x, y, z) == 2;
    }
}
