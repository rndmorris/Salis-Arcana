package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.tiles.TileArcaneBore;

@Mixin(TileArcaneBore.class)
abstract class MixinTileArcaneBore_DecreaseCVisCheckFrequency {

    @Shadow(remap = false)
    public abstract boolean gettingPower();

    @Shadow(remap = false)
    private float speedyTime;

    @Unique
    private int salisarcana$cooldown = 0;
    @Unique
    private float salisarcana$cachedTime = 0;

    // Add a cooldown to the if statement.
    @ModifyExpressionValue(
        method = "updateEntity",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isRemote:Z",
            ordinal = 0,
            opcode = Opcodes.GETFIELD))
    public boolean powerAndSleep(boolean isRemote) {
        // The returned value is inverted.

        // Client thread, return
        if (isRemote) return true;

        // Cooldown expired, do checks
        if (salisarcana$cooldown-- <= 0) {
            if (this.gettingPower()) {
                // Do a speedyTime refill, sleep 10s
                salisarcana$cooldown = 10 * 20;
            } else {
                // Block isn't powered, so do speedyTime refill but choke to 30s instead
                salisarcana$cooldown = 30 * 20;
            }
            // double cooldown if it never had cvis
            // imprecision means it will never be 0 again. I think.
            salisarcana$cooldown *= this.speedyTime == 0 ? 2 : 1;
            return false;
        }

        // if it has any cached speedyTime, but less than the max, skip cooldown
        // Speedy time can be exhausted in 1 second by a constantly-digging bore.
        // if the speedyTime doesn't change since last lookup, increase cooldown to 1/2s
        if (this.speedyTime != 0 && this.speedyTime < 20) {
            if (this.speedyTime == salisarcana$cachedTime) {
                salisarcana$cooldown = Math.min(salisarcana$cooldown, 10);
            } else salisarcana$cooldown = 0;
            salisarcana$cachedTime = this.speedyTime;
        }

        // Cooldown's not up, don't refill speedyTime.
        return true;
    }

}
