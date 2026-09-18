package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;

import dev.rndmorris.salisarcana.lib.EventUtils;
import dev.rndmorris.salisarcana.lib.PlayerHelper;
import thaumcraft.common.tiles.TileHole;
import thaumcraft.common.tiles.TileMemory;

@Mixin(TileHole.class)
abstract class MixinTileHole_StoreCreator extends TileMemory {

    @Shadow
    public short countdown;
    @Shadow
    public byte count;
    @Shadow
    public byte direction;

    @Unique
    private @Nullable GameProfile salisArcana$ownerProfile;

    @Inject(method = "<init>(Lnet/minecraft/block/Block;ISBBLnet/minecraft/tileentity/TileEntity;)V", at = @At("TAIL"))
    private void captureCreator(Block bi, int md, short max, byte count, byte direction, TileEntity te,
        CallbackInfo ci) {
        final EntityPlayer owner = EventUtils.portableHoleOwner;
        if (owner != null) {
            this.salisArcana$ownerProfile = owner.getGameProfile();
        }
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void loadOwner(NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.getTag("salisarcana:owner") instanceof NBTTagCompound ownerInfo) {
            this.salisArcana$ownerProfile = PlayerHelper.gameProfileFromNBT(ownerInfo);
        } else {
            this.salisArcana$ownerProfile = null;
        }
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void saveOwner(NBTTagCompound nbt, CallbackInfo ci) {
        final GameProfile profile = this.salisArcana$ownerProfile;
        if (profile != null) {
            nbt.setTag("salisarcana:owner", PlayerHelper.gameProfileToNBT(profile));
        }
    }

    @WrapMethod(method = "updateEntity")
    private void setOwnerInfo(Operation<Void> original) {
        if (this.countdown == 0 && this.count > 1
            && this.direction != -1
            && this.salisArcana$ownerProfile != null
            && this.worldObj instanceof WorldServer world) {

            EventUtils.portableHoleOwner = PlayerHelper.getOrFakePlayer(this.salisArcana$ownerProfile, world);
            try {
                original.call();
            } finally {
                EventUtils.portableHoleOwner = null;
            }
        } else {
            original.call();
        }
    }
}
