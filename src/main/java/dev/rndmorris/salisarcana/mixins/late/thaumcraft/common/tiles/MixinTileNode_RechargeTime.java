package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.TimeHelper;
import dev.rndmorris.salisarcana.lib.ifaces.IGameTimeNode;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class)
public abstract class MixinTileNode_RechargeTime extends TileThaumcraft implements IGameTimeNode {

    @Shadow(remap = false)
    public abstract NodeModifier getNodeModifier();

    @Unique
    long sa$lastTickActive = -1;

    /**
     * Persist our custom field to NBT.
     */
    @WrapMethod(method = "writeToNBT")
    private void writeToNBT(NBTTagCompound nbttagcompound, Operation<Void> original) {
        original.call(nbttagcompound);
        if (sa$lastTickActive >= 0) {
            nbttagcompound.setLong("salisarcana:lastTickActive", sa$lastTickActive);
        }
    }

    /**
     * Re-load our custom field from NBT, if it was previously persisted there.
     */
    @WrapMethod(method = "readFromNBT")
    private void readFromNBT(NBTTagCompound nbttagcompound, Operation<Void> original) {
        original.call(nbttagcompound);
        if (nbttagcompound.hasKey("salisarcana:lastTickActive", Constants.NBT.TAG_LONG)) {
            sa$lastTickActive = nbttagcompound.getLong("salisarcana:lastTickActive");
        }
    }

    /**
     * If Salis has not yet taken over catch-up recharging, let TC do its thing. If not, verify that enough game
     * time has elapsed to merit catch-up recharging.
     */
    @WrapOperation(
        method = "handleRecharge",
        remap = false,
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;catchUp:Z", opcode = Opcodes.GETFIELD))
    private boolean shouldCatchUp(TileNode instance, Operation<Boolean> original) {
        boolean catchUp = original.call(instance);
        if (sa$lastTickActive < 0) {
            // we don't have a recorded last active world time, so just let TC do its thing
            return catchUp;
        }
        if (!catchUp) {
            // if system time hasn't progressed enough for vanilla TC to regen
            // it's highly unlikely world time has progressed
            return false;
        }
        long regenRate = sa$regenRate();
        long regenInterval = regenRate * 75L;
        long worldTime = worldObj.getTotalWorldTime();
        return regenRate > 0 && TimeHelper.ticksToMs(worldTime) > sa$lastTickActive + regenInterval;
    }

    /**
     * If Salis has taken over catch-up recharging, use the game time for checking how long the node has been unloaded.
     * Otherwise, let TC do its thing.
     */
    @WrapOperation(
        method = "handleRecharge",
        remap = false,
        at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 0))
    private long captureCurrentTimeForRegen(Operation<Long> original) {
        return sa$lastTickActive >= 0 ? TimeHelper.ticksToMs(worldObj.getTotalWorldTime()) : original.call();
    }

    /**
     * If Salis has taken over catch-up recharging, use our saved game time for getting the last time the node was
     * active. Otherwise let TC do its thing.
     */
    @WrapOperation(
        method = "handleRecharge",
        remap = false,
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;lastActive:J", opcode = Opcodes.GETFIELD))
    private long captureLastActiveForRegen(TileNode instance, Operation<Long> original) {
        return sa$lastTickActive >= 0 ? TimeHelper.ticksToMs(sa$lastTickActive) : original.call(instance);
    }

    /**
     * Store when the node last recharged, and open the door for Salis to take over catch-up recharging if it hasn't
     * already.
     */
    @WrapOperation(
        method = "handleRecharge",
        remap = false,
        at = @At(value = "FIELD", target = "Lthaumcraft/common/tiles/TileNode;lastActive:J", opcode = Opcodes.PUTFIELD))
    private void captureCurrentTimeForStorage(TileNode instance, long value, Operation<Void> original) {
        original.call(instance, value);
        sa$updateLastTickActive();
    }

    /**
     * Utility, put here just to keep the primary method clean
     */
    @Unique
    private long sa$regenRate() {
        var modifier = getNodeModifier();
        if (modifier == null) {
            return 600;
        }
        return switch (modifier) {
            case BRIGHT -> 400;
            case PALE -> 900;
            case FADING -> 0;
            default -> 600;
        };
    }

    @Override
    public void sa$updateLastTickActive() {
        sa$lastTickActive = worldObj.getTotalWorldTime();
    }
}
