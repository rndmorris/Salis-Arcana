package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.tiles.TileNode;

@Mixin(TileNode.class)
public class MixinTileNode_ProtectGetBlock extends TileEntity {

    @Redirect(
        method = "handleDischarge",
        remap = false,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;",
            remap = true))
    private TileEntity protectGetTE(World instance, int x, int y, int z) {
        if (this.worldObj.blockExists(x, y, z)) {
            return this.worldObj.getTileEntity(x, y, z);
        }
        return null;
    }

    @Inject(
        method = "handlePureNode",
        remap = false,
        cancellable = true,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBiomeGenForCoords(II)Lnet/minecraft/world/biome/BiomeGenBase;",
            remap = true))
    private void protectGetBiome(boolean change, CallbackInfoReturnable<Boolean> cir, @Local(name = "x") int x,
        @Local(name = "z") int z) {
        if (!this.worldObj.blockExists(x, 0, z)) {
            cir.setReturnValue(change);
        }
    }

    @Inject(
        method = "handleHungryNodeSecond",
        remap = false,
        cancellable = true,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Vec3;createVectorHelper(DDD)Lnet/minecraft/util/Vec3;",
            ordinal = 0,
            remap = true))
    private void protectGetBlock(boolean change, CallbackInfoReturnable<Boolean> cir, @Local(name = "tx") int tx,
        @Local(name = "ty") int ty, @Local(name = "tz") int tz) {
        if (!this.worldObj.blockExists(tx, ty, tz)) {
            cir.setReturnValue(change);
        }
    }
}
