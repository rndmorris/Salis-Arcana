package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.network.playerdata;

import net.minecraft.client.entity.EntityClientPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;

@Mixin(PacketWarpMessage.class)
public class MixinPacketWarpMessage_MuteExcessiveSounds {

    @Unique
    private static long salisArcana$lastTickTriggered;

    @WrapWithCondition(
        method = "onMessage(Lthaumcraft/common/lib/network/playerdata/PacketWarpMessage;Lcpw/mods/fml/common/network/simpleimpl/MessageContext;)Lcpw/mods/fml/common/network/simpleimpl/IMessage;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;playSound(Ljava/lang/String;FF)V"))
    private boolean shouldTriggerSound(EntityClientPlayerMP player, String sound, float volume, float pitch) {
        if (salisArcana$lastTickTriggered < (player.worldObj.getTotalWorldTime() - 5)) {
            salisArcana$lastTickTriggered = player.worldObj.getTotalWorldTime();
            return true;
        } else {
            return false;
        }
    }

}
