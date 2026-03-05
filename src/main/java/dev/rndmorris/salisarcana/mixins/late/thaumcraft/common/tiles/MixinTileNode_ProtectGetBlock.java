package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.tiles.TileNode;

/**
 * The tick code of TileNode has multiple calls to world.getBlock, world.getTileEntity...
 * with coordinates that's around the Node, thing is if those coordinates happen to be in
 * unloaded chunks, the call to getBlock will trigger chunk loading on the server which
 * is bad for performance.
 * This mixin prevents this behavior by adding worldObj.blockExists checks before
 * running the code that would trigger chunk loading.
 */
@Mixin(TileNode.class)
public abstract class MixinTileNode_ProtectGetBlock extends TileEntity {

    @Inject(
        method = "handleDischarge",
        remap = false,
        cancellable = true,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;",
            remap = true))
    private void protectGetTE(boolean change, CallbackInfoReturnable<Boolean> cir, @Local(name = "x") int x,
        @Local(name = "y") int y, @Local(name = "z") int z) {
        if (!this.worldObj.blockExists(this.xCoord + x, this.yCoord + y, this.zCoord + z)) {
            cir.setReturnValue(change);
        }
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
