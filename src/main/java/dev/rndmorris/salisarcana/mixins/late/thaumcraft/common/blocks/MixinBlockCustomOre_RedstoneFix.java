package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import thaumcraft.common.blocks.BlockCustomOre;

@Mixin(BlockCustomOre.class)
public abstract class MixinBlockCustomOre_RedstoneFix {

    /**
     * @author Midnight145
     * @reason Ore blocks should be able to pass a redstone signal
     */
    @Overwrite
    public boolean renderAsNormalBlock() {
        return true;
    }
}
