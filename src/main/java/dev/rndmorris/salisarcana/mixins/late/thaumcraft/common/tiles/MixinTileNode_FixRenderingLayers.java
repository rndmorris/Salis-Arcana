package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.tiles.TileEldritchPortal;

@Mixin(TileEldritchPortal.class)
public abstract class MixinTileNode_FixRenderingLayers extends TileEntity {

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
}
