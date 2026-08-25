package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.research;

import java.util.Map;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import thaumcraft.common.lib.research.PlayerKnowledge;

@Mixin(value = PlayerKnowledge.class, remap = false)
abstract class MixinPlayerKnowledge_DebugLogWarp {

    @Unique
    private static final Logger salisArcana$warpLogger = LogManager.getLogger("Thaumcraft Warp Debug");

    @Shadow
    public Map<String, Integer> warp;

    @Shadow
    public Map<String, Integer> warpSticky;

    @Shadow
    public Map<String, Integer> warpTemp;

    @Inject(method = "addWarpPerm", at = @At("TAIL"))
    private void logAddPerm(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Add " + amount + " PERM");
    }

    @Inject(method = "addWarpSticky", at = @At("TAIL"))
    private void logAddSticky(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Add " + amount + " STICKY");
    }

    @Inject(method = "addWarpTemp", at = @At("TAIL"))
    private void logAddTemp(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Add " + amount + " TEMP");
    }

    @Inject(method = "setWarpPerm", at = @At("TAIL"))
    private void logSetPerm(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Set " + amount + " PERM");
    }

    @Inject(method = "setWarpSticky", at = @At("TAIL"))
    private void logSetSticky(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Set " + amount + " STICKY");
    }

    @Inject(method = "setWarpTemp", at = @At("TAIL"))
    private void logSetTemp(String player, int amount, CallbackInfo ci) {
        salisArcana$logWarp(player, "Set " + amount + " TEMP");
    }

    @Unique
    private void salisArcana$logWarp(String player, String message) {
        final Side side = FMLCommonHandler.instance()
            .getEffectiveSide();
        final StackTraceElement[] stackTrace = Thread.currentThread()
            .getStackTrace();
        final StringJoiner stackLog = new StringJoiner("\n\t");

        // Skip the first 3 elements of the stack trace
        for (int i = 3; i < stackTrace.length; i++) {
            if (side == Side.CLIENT
                && "thaumcraft.common.lib.network.playerdata.PacketSyncWarp".equals(stackTrace[i].getClassName())
                && "onMessage".equals(stackTrace[i].getMethodName())) {

                // This was a sync packet, most likely. No need to log the rest of the stack trace.
                // It *is* still a good idea to log other client-side warp effects, since those are usually a bug.
                stackLog.add("Caused by synchronization via PacketSyncWarp");
                break;
            }

            stackLog.add(stackTrace[i].toString());
        }

        // Use only one logging call, so the message doesn't get interleaved with logs from other threads.
        salisArcana$warpLogger.info(
            "Thread: \"{}\" | Player: {} | {} | Is Now: {} PERM {} STICKY {} TEMP | Stack Trace:\n\t{}",
            Thread.currentThread()
                .getName(),
            player,
            message,
            this.warp.getOrDefault(player, 0),
            this.warpSticky.getOrDefault(player, 0),
            this.warpTemp.getOrDefault(player, 0),
            stackLog.toString());
    }
}
