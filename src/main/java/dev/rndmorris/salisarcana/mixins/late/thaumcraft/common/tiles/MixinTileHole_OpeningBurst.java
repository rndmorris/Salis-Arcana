package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileHole;

@Mixin(value = TileHole.class, remap = false)
public abstract class MixinTileHole_OpeningBurst extends TileEntity {

    @Shadow
    public short countdown;

    @Shadow
    public byte direction;

    @Unique
    private boolean sa$playedOpeningBurst;

    @Inject(method = "updateEntity", at = @At("HEAD"))
    private void playOpeningBurst(CallbackInfo ci) {
        // Child holes are created with direction == -1 during expansion, so only the original target should burst.
        if (this.worldObj != null && this.worldObj.isRemote
            && !this.sa$playedOpeningBurst
            && this.direction != -1
            && this.countdown <= 1) {
            this.sa$playedOpeningBurst = true;
            Thaumcraft.proxy.blockSparkle(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 4194368, 1);
        }
    }
}
