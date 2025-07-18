package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.blocks.BlockCrystal;

@Mixin(BlockCrystal.class)
public abstract class MixinBlockCrystal extends Block {

    protected MixinBlockCrystal(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return true;
    }
}
