package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.blocks.BlockWoodenDevice;
import thaumcraft.common.tiles.TileBanner;

@Mixin(BlockWoodenDevice.class)
public abstract class MixinBlockWoodenDevice_BannerPickBlock extends BlockContainer {

    protected MixinBlockWoodenDevice_BannerPickBlock(Material materialIn) {
        super(materialIn);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        if (world.getBlockMetadata(x, y, z) == 8 && world.getTileEntity(x, y, z) instanceof TileBanner banner) {
            ItemStack item = new ItemStack(this, 1, 8);
            if (banner.getColor() >= 0) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("color", banner.getColor());
                if (banner.getAspect() != null) {
                    nbt.setString(
                        "aspect",
                        banner.getAspect()
                            .getTag());
                }
                item.setTagCompound(nbt);
            }
            return item;
        }

        return super.getPickBlock(target, world, x, y, z);
    }
}
