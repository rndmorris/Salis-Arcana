package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import thaumcraft.client.renderers.tile.TileEldritchCapRenderer;
import thaumcraft.client.renderers.tile.TileFocalManipulatorRenderer;
import thaumcraft.client.renderers.tile.TileThaumatoriumRenderer;

@Mixin({ TileEldritchCapRenderer.class, TileFocalManipulatorRenderer.class, TileThaumatoriumRenderer.class })
public abstract class MixinTESR_FixLeak extends TileEntitySpecialRenderer {

    @Shadow(remap = false)
    EntityItem entityitem;

    @Override
    public void func_147496_a(World p_147496_1_) {
        this.entityitem = null;
    }
}
