package dev.rndmorris.tfixins.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.blocks.BlockCustomOre;

@Mixin(BlockCustomOre.class)
public abstract class MixinBlockCustomOre {

    /**
     * @author Midnight145
     * @reason Ore blocks should be able to pass a redstone signal
     */
    @Overwrite
    public boolean renderAsNormalBlock() {
        return true;
    }
}
