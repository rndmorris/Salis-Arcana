package dev.rndmorris.salisarcana.mixins.late.tiles;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import thaumcraft.common.tiles.TileEtherealBloom;

@Mixin(value = TileEtherealBloom.class, remap = false)
public class MixinTileEtherealBloom extends TileEntity {

    @Shadow
    public int counter;

    @Shadow
    public int growthCounter;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.counter = compound.getInteger("counter");

        this.growthCounter = compound.getInteger("growthCounter");

        // might as well prevent some overflows here

        // counter is checked for % 32 in TileEtherealBloomRenderer, and % 20 in TileEtherealBloom
        this.counter %= 160; // (160 is LCM of 32 and 20)

        // it's bound to a > 100 check in TileEtherealBloomRenderer
        this.growthCounter = Math.min(101, this.growthCounter);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("counter", this.counter);
        compound.setInteger("growthCounter", this.growthCounter);

        this.counter %= 160;
        this.growthCounter = Math.min(101, this.growthCounter);
        super.writeToNBT(compound);
    }

    // Minecraft doesn't sync NBT data between client and server for TileEntities, so we need to do it ourselves

    @Nullable
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
}
