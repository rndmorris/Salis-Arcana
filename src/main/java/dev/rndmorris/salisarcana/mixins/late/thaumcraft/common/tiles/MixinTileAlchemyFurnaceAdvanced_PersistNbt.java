package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvanced;

@Mixin(value = TileAlchemyFurnaceAdvanced.class)
public abstract class MixinTileAlchemyFurnaceAdvanced_PersistNbt extends TileThaumcraft {

    @Unique
    private final static String sa$keyCount = "salisarcana:count";
    @Unique
    private final static String sa$keyProcessed = "salisarcana:processed";

    @Shadow(remap = false)
    int count;
    @Shadow(remap = false)
    int processed;

    @Inject(method = "readFromNBT", at = @At(value = "TAIL"))
    void readAdditionalFields(NBTTagCompound nbtCompound, CallbackInfo ci) {
        // the furnace rolls over back to 0 at 5
        final int countLimit = 5;

        try {
            count = nbtCompound.hasKey(sa$keyCount, Constants.NBT.TAG_INT) ? nbtCompound.getInteger(sa$keyCount)
                // The TE's worldObj is usually null when the TE is first being loaded
                // in my experience, so we need fallbacks
                : (worldObj != null && worldObj.rand != null ? worldObj.rand
                    : WorldProvider.getProviderForDimension(0).worldObj.rand).nextInt(countLimit);
        } catch (RuntimeException re) {
            // technically a possibility with `getProviderForDimension`,
            // but it should hopefully never happen
            count = 0;
        } ;
        processed = nbtCompound.getInteger(sa$keyProcessed);
    }

    @Inject(method = "writeToNBT", at = @At(value = "TAIL"))
    void writeAdditionalFields(NBTTagCompound nbtTagCompound, CallbackInfo ci) {
        nbtTagCompound.setInteger(sa$keyCount, count);
        nbtTagCompound.setInteger(sa$keyProcessed, processed);
    }
}
