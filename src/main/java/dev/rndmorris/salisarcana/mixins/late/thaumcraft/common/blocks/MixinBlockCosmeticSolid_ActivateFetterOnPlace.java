package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.blocks.BlockCosmeticSolid;

@Mixin(BlockCosmeticSolid.class)
abstract class MixinBlockCosmeticSolid_ActivateFetterOnPlace extends Block {

    protected MixinBlockCosmeticSolid_ActivateFetterOnPlace(Material materialIn) {
        super(materialIn);
    }

    @Override
    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ,
        int meta) {
        if (meta == 9 && worldIn.isBlockIndirectlyGettingPowered(x, y, z)) {
            return 10;
        } else {
            return super.onBlockPlaced(worldIn, x, y, z, side, subX, subY, subZ, meta);
        }
    }
}
