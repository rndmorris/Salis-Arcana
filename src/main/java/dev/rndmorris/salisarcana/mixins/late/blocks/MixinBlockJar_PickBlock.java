package dev.rndmorris.salisarcana.mixins.late.blocks;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import thaumcraft.common.blocks.BlockJar;

@Mixin(BlockJar.class)
public abstract class MixinBlockJar_PickBlock extends BlockContainer {

    @Shadow(remap = false)
    public abstract ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune);

    protected MixinBlockJar_PickBlock(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        final var drop = this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        if (drop.size() == 1) {
            return drop.get(0);
        } else {
            return super.getPickBlock(target, world, x, y, z);
        }
    }
}
