package dev.rndmorris.tfixins.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;

import com.midnight.metaawareblocks.api.IMetaAware;

import thaumcraft.common.blocks.BlockMetalDevice;

@Mixin(BlockMetalDevice.class)
public abstract class MixinBlockMetalDevice implements IMetaAware {

    @Override
    public boolean renderAsNormalBlock(int meta) {
        // Advanced Alchemical Construct, Alchemical Construct (full blocks)
        return meta == 9 || meta == 3;
    }
}
