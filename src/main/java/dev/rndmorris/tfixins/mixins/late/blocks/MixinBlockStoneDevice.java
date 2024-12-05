package dev.rndmorris.tfixins.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;

import com.midnight.metaawareblocks.api.IMetaAware;

import thaumcraft.common.blocks.BlockStoneDevice;

@Mixin(BlockStoneDevice.class)
public abstract class MixinBlockStoneDevice implements IMetaAware {

    @Override
    public boolean renderAsNormalBlock(int meta) {
        // Alchemical Furnace, Arcane Spa (full blocks)
        return meta == 0 || meta == 12;
    }
}
