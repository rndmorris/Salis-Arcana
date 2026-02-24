package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.blocks.BlockAlchemyFurnace;
import thaumcraft.common.blocks.BlockArcaneFurnace;
import thaumcraft.common.blocks.BlockMetalDevice;
import thaumcraft.common.blocks.BlockStoneDevice;
import thaumcraft.common.blocks.BlockTube;
import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin({ BlockAiry.class, BlockAlchemyFurnace.class, BlockArcaneFurnace.class, BlockMetalDevice.class,
    BlockStoneDevice.class, BlockTube.class, BlockWoodenDevice.class })
public abstract class MixinBlock_CollisionConserveBlockBounds {

    @Redirect(method = "addCollisionBoxesToList", at = @At(value = "INVOKE", target = "*(FFFFFF)V", remap = false))
    private void addBoundingBox(@Coerce Block instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, World world, int x, int y, int z, AxisAlignedBB mask, List<AxisAlignedBB> list) {
        // Fun fact: zero-volume hitboxes are still collide-able - which is why the Banner has such strange hitboxes.
        if (minX == maxX || minY == maxY || minZ == maxZ) return;

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);

        if (box.intersectsWith(mask)) {
            list.add(box);
        }
    }

    @Redirect(
        method = "addCollisionBoxesToList",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockContainer;addCollisionBoxesToList(Lnet/minecraft/world/World;IIILnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V"))
    private void skipAddCollisionBoxes(BlockContainer instance, World world, int x, int y, int z, AxisAlignedBB mask,
        List<AxisAlignedBB> list, Entity entity) {
        // This method intentionally left blank.
    }
}
