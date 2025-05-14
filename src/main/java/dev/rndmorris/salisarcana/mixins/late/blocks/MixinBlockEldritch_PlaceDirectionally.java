package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch_PlaceDirectionally {

    // @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        int meta = worldIn.getBlockMetadata(x, y, z);

        if (meta == 9) {
            if (worldIn.getTileEntity(x, y, z) instanceof TileEldritchCrabSpawner crabSpawner) {
                crabSpawner.setFacing((byte) BlockPistonBase.determineOrientation(worldIn, x, y, z, placer));
            }
        } else if (meta == 8) {
            if (worldIn.getTileEntity(x, y, z) instanceof TileEldritchLock lock) {
                int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                lock.setFacing((byte) (facing == 0 ? 2 : (facing == 1 ? 5 : (facing == 2 ? 3 : 4))));
            }
        }
    }
}
