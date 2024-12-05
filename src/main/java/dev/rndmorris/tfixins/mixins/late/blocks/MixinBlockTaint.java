package dev.rndmorris.tfixins.mixins.late.blocks;

import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;

import com.midnight.metaawareblocks.api.IMetaAware;

import thaumcraft.common.blocks.BlockTaint;

@Mixin(BlockTaint.class)
public abstract class MixinBlockTaint implements IMetaAware {

    @Override
    public boolean renderAsNormalBlock(IBlockAccess world, int x, int y, int z) {
        // flesh blocks
        return world.getBlockMetadata(x, y, z) == 2;
    }
}
