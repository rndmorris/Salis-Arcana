package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileWarded;

@Mixin(TileWarded.class)
abstract class MixinTileWarded_DontStoreMetadata extends TileThaumcraft {

    @Definition(id = "getByte", method = "Lnet/minecraft/nbt/NBTTagCompound;getByte(Ljava/lang/String;)B", remap = true)
    @Expression("?.getByte('md')")
    @Redirect(method = "readCustomNBT", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private byte getMetadataFromWorld(NBTTagCompound instance, String s) {
        return (byte) this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    @Definition(
        id = "setByte",
        method = "Lnet/minecraft/nbt/NBTTagCompound;setByte(Ljava/lang/String;B)V",
        remap = true)
    @Expression("?.setByte('md', ?)")
    @Redirect(method = "writeCustomNBT", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private void dontStoreMetadata(NBTTagCompound instance, String key, byte value) {
        // This method intentionally left blank.
    }
}
