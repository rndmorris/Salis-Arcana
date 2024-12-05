package dev.rndmorris.tfixins.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;

import com.midnight.metaawareblocks.api.IMetaAware;

import thaumcraft.common.blocks.BlockCustomOre;

@Mixin(BlockCustomOre.class)
public abstract class MixinBlockCustomOre implements IMetaAware {

    @Override
    public boolean renderAsNormalBlock(int meta) {
        return meta != 15;
    }
}
