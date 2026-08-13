package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.lib.utils.BlockUtils;

@Mixin(value = BlockUtils.class, remap = false)
abstract class MixinBlockUtils_PreserveTileEntityDrops {

    @WrapOperation(
        method = "harvestBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;IIIZI)Z",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/utils/BlockUtils;removeBlock(Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;)Z",
            ordinal = 1,
            remap = false),
        remap = false)
    private static boolean preserveTileEntityUntilHarvest(World world, int x, int y, int z, EntityPlayer player,
        Operation<Boolean> original, @Local(name = "flag1") boolean canHarvest) {
        if (!canHarvest) return original.call(world, x, y, z, player);

        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        block.onBlockHarvested(world, x, y, z, metadata, player);

        boolean removed = block.removedByPlayer(world, player, x, y, z, true);
        if (removed) {
            block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
        }
        return removed;
    }
}
