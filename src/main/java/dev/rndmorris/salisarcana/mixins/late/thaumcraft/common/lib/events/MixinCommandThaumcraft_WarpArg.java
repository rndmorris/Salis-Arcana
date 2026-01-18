package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.events.CommandThaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;

@Mixin(CommandThaumcraft.class)
public class MixinCommandThaumcraft_WarpArg {

    @Inject(method = "setWarp", at = @At("HEAD"), remap = false)
    private void warpAllParams(ICommandSender icommandsender, EntityPlayerMP player, int i, String type,
        CallbackInfo ci) {
        if (type.equalsIgnoreCase("ALL")) {
            Thaumcraft.proxy.playerKnowledge.setWarpPerm(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 0), player);

            Thaumcraft.proxy.playerKnowledge.setWarpTemp(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 2), player);

            Thaumcraft.proxy.playerKnowledge.setWarpSticky(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 1), player);
        }
    }

    @Inject(method = "addWarp", at = @At("HEAD"), remap = false)
    private void addWarpAllParams(ICommandSender icommandsender, EntityPlayerMP player, int i, String type,
        CallbackInfo ci) {
        if (type.equalsIgnoreCase("ALL")) {
            Thaumcraft.proxy.playerKnowledge.addWarpPerm(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 0), player);
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte) 0, i), player);

            Thaumcraft.proxy.playerKnowledge.addWarpTemp(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 2), player);
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte) 2, i), player);

            Thaumcraft.proxy.playerKnowledge.addWarpSticky(player.getCommandSenderName(), i);
            PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 1), player);
            PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte) 1, i), player);
        }
    }

    @ModifyConstant(
        method = "processCommand",
        constant = @Constant(stringValue = "  /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"))
    private String modifyWarpCommandHelp(String original) {
        return "  /thaumcraft warp <player> <add|set> <amount> [PERM|TEMP|ALL]";
    }

    @ModifyConstant(
        method = "processCommand",
        constant = @Constant(stringValue = "§cUse /thaumcraft warp <player> <add|set> <amount> <PERM|TEMP>"))
    private String modifyWarpInvalidUsage(String original) {
        return "§cUse /thaumcraft warp <player> <add|set> <amount> [PERM|TEMP|ALL]";
    }
}
