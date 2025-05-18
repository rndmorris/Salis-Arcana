package dev.rndmorris.salisarcana.mixins.late.blocks;

import dev.rndmorris.salisarcana.config.SalisConfig;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.BlockEldritch;
import thaumcraft.common.tiles.TileEldritchCrabSpawner;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch_PlaceDirectionally extends BlockContainer {

    protected MixinBlockEldritch_PlaceDirectionally(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);
    }

    @Dynamic
    @Inject(method = "onBlockPlacedBy(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    public void placeDirectionally(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn, CallbackInfo ci) {
        int meta = worldIn.getBlockMetadata(x, y, z);

        if (meta == 9) {
            if (worldIn.getTileEntity(x, y, z) instanceof TileEldritchCrabSpawner crabSpawner) {
                crabSpawner.setFacing((byte) BlockPistonBase.determineOrientation(worldIn, x, y, z, placer));
            }
        } else if (meta == 8) {
            if (worldIn.getTileEntity(x, y, z) instanceof TileEldritchLock lock) {
                int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                int directionID = facing == 0 ? 2 : (facing == 1 ? 5 : (facing == 2 ? 3 : 4));
                lock.setFacing((byte) directionID);
            }
        }
    }
}
