package dev.rndmorris.salisarcana.mixins.late.blocks;

import static dev.rndmorris.salisarcana.config.SalisConfig.bugfixes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import thaumcraft.common.blocks.BlockCosmeticSolid;

@Mixin(BlockCosmeticSolid.class)
public abstract class MixinBlockCosmeticSolid extends Block {

    protected MixinBlockCosmeticSolid(Material materialIn) {
        super(materialIn);
    }

    /**
     * @author rndmorris
     * @reason Some {@link BlockCosmeticSolid}s should be beacon bases - the others shouldn't.
     */
    @Overwrite(remap = false)
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        final var metadata = worldObj.getBlockMetadata(x, y, z);

        return bugfixes.beaconBlockFixSetting.isBeaconMetadata(metadata);
    }
}
