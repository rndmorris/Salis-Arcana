package dev.rndmorris.salisarcana.mixins.late.blocks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public abstract class MixinBlockEldritch_PlaceFullObelisk extends BlockContainer {
    protected MixinBlockEldritch_PlaceFullObelisk(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Override
    public boolean canReplace(World worldIn, int x, int y, int z, int side, ItemStack itemIn) {
        return super.canReplace(worldIn, x, y, z, side, itemIn);
    }

    @Dynamic(mixin = MixinBlockEldritch_PlaceFullObelisk.class)
    @WrapMethod(method = "canReplace(Lnet/minecraft/world/World;IIIILnet/minecraft/item/ItemStack;)Z")
    public boolean canPlaceFullObelisk(World worldIn, int x, int y, int z, int side, ItemStack itemIn, Operation<Boolean> original) {
        if(itemIn.getItemDamage() == 1) {
            return super.canPlaceBlockAt(worldIn, x, y, z)
                && super.canPlaceBlockAt(worldIn, x, y + 1, z)
                && super.canPlaceBlockAt(worldIn, x, y + 2, z)
                && super.canPlaceBlockAt(worldIn, x, y + 3, z)
                && super.canPlaceBlockAt(worldIn, x, y + 4, z);
        } else {
            return original.call(worldIn, x, y, z, side, itemIn);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);
    }

    @Dynamic(mixin = MixinBlockEldritch_PlaceFullObelisk.class)
    @Inject(method = "onBlockPlacedBy(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    public void placeFullObelisk(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn, CallbackInfo ci) {
        if(itemIn.getItemDamage() == 1) {
            worldIn.setBlock(x, y + 1, z, this, 2, 3);
            worldIn.setBlock(x, y + 2, z, this, 2, 3);
            worldIn.setBlock(x, y + 3, z, this, 2, 3);
            worldIn.setBlock(x, y + 4, z, this, 2, 3);
        }
    }
}
