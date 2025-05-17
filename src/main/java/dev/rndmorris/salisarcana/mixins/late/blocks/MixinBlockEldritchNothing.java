package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.blocks.BlockEldritchNothing;
import thaumcraft.common.lib.utils.BlockUtils;

@Mixin(BlockEldritchNothing.class)
public abstract class MixinBlockEldritchNothing extends Block {
    protected MixinBlockEldritchNothing(Material materialIn) {
        super(materialIn);
    }

    @Override
    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta) {
        return BlockUtils.isBlockExposed(worldIn, x, y, z) ? 1 : 0;
    }
}
