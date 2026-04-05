package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class, remap = false)
public abstract class MixinTileCrucible_PressureMechanic extends TileThaumcraft {

    @Unique
    public int thaumicEnlightenment$spillPressure = 0;
    @Unique
    public final int thaumicEnlightenment$pressureEffectRange = 8;
    @Unique
    public boolean thaumicEnlightenment$isMeltingDown = false;
    @Unique
    private boolean thaumcraft$spillSucceeded = false;

    @Shadow
    public abstract int tagAmount();

    @Inject(method = "readCustomNBT", at = @At("TAIL"))
    private void spill$readNBT(NBTTagCompound nbt, CallbackInfo ci) {
        this.thaumicEnlightenment$spillPressure = nbt.getInteger("SpillPressure");
    }

    @Inject(method = "writeCustomNBT", at = @At("TAIL"))
    private void spill$writeNBT(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setInteger("SpillPressure", this.thaumicEnlightenment$spillPressure);
    }

    @Inject(method = "spill", at = @At("HEAD"))
    private void spill$head(CallbackInfo ci) {
        thaumcraft$spillSucceeded = false;
    }

    // Covers all six setBlock call sites inside spill():
    // ordinal 0 — air above: setBlock fluxGas
    // ordinal 1 — air above: setBlock fluxGoo
    // ordinal 2 — md < 7: setBlock fluxGoo (increment)
    // ordinal 3 — md < 7: setBlock fluxGas (increment)
    // ordinal 4 — random offset: setBlock fluxGas
    // ordinal 5 — random offset: setBlock fluxGoo
    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 0),
        require = 0)
    private void spill$setBlock0(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 1),
        require = 0)
    private void spill$setBlock1(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 2),
        require = 0)
    private void spill$setBlock2(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 3),
        require = 0)
    private void spill$setBlock3(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 4),
        require = 0)
    private void spill$setBlock4(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(
        method = "spill",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 5),
        require = 0)
    private void spill$setBlock5(CallbackInfo ci) {
        thaumcraft$spillSucceeded = true;
    }

    @Inject(method = "spill", at = @At("TAIL"))
    private void spill$tail(CallbackInfo ci) {
        if (!thaumcraft$spillSucceeded) {
            thaumicEnlightenment$spillPressure++;
            this.thaumicEnlightenment$onSpillPressureChanged();
        } else if (thaumicEnlightenment$spillPressure > 0) {
            thaumicEnlightenment$spillPressure = Math.max(thaumicEnlightenment$spillPressure - 3, 0);
        }
    }

    @Unique
    private void thaumicEnlightenment$onSpillPressureChanged() {
        if (this.worldObj.rand.nextInt(20) > 1) return;
        if (thaumicEnlightenment$spillPressure >= 100 && !this.worldObj.isRemote
            && !thaumicEnlightenment$isMeltingDown) {
            this.thaumicEnlightenment$explode();
        } else if (thaumicEnlightenment$spillPressure >= 10) {
            this.thaumicEnlightenment$radiateTaint();
        }
    }

    @Unique
    private void thaumicEnlightenment$explode() {
        thaumicEnlightenment$isMeltingDown = true;

        float explosionStrength = Math.min(4.0F + (this.tagAmount() - 100) / 50.0F, 6.0F);
        this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, explosionStrength, true);
        this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    }

    @Unique
    private void thaumicEnlightenment$radiateTaint() {
        int randX = this.xCoord + this.worldObj.rand.nextInt(thaumicEnlightenment$pressureEffectRange * 2)
            - thaumicEnlightenment$pressureEffectRange;
        int randZ = this.zCoord + this.worldObj.rand.nextInt(thaumicEnlightenment$pressureEffectRange * 2)
            - thaumicEnlightenment$pressureEffectRange;

        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(randX, randZ);
        if (biome.biomeID != ThaumcraftWorldGenerator.biomeTaint.biomeID) {
            Utils.setBiomeAt(this.worldObj, randX, randZ, ThaumcraftWorldGenerator.biomeTaint);
        }

        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
            this.xCoord - thaumicEnlightenment$pressureEffectRange,
            this.yCoord - thaumicEnlightenment$pressureEffectRange,
            this.zCoord - thaumicEnlightenment$pressureEffectRange,
            this.xCoord + thaumicEnlightenment$pressureEffectRange + 1,
            this.yCoord + thaumicEnlightenment$pressureEffectRange + 1,
            this.zCoord + thaumicEnlightenment$pressureEffectRange + 1);
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
        for (EntityPlayer player : players) {
            Thaumcraft.proxy.getPlayerKnowledge()
                .addWarpTemp(
                    player.getGameProfile()
                        .getName(),
                    1);
            player.addChatMessage(new ChatComponentTranslation("salisarcana:misc.cruciblePressureRadiation"));
        }
    }
}
