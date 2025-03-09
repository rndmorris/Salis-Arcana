package dev.rndmorris.salisarcana.mixins.late.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.Thaumcraft;

@Mixin(Thaumcraft.class)
public class MixinThaumcraft_FakePlayerWarp {

    @Inject(method = "addWarpToPlayer", at = @At("HEAD"), remap = false, cancellable = true)
    private static void cancelFakePlayers(EntityPlayer player, int amount, boolean temporary, CallbackInfo ci) {
        if (!(player instanceof EntityPlayerMP) || player instanceof FakePlayer) {
            ci.cancel();
            final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
            final String name = player.getCommandSenderName();
            if (name != null && knowledge != null) {
                if (temporary) {
                    knowledge.addWarpTemp(name, amount);
                } else if (amount > 0) {
                    knowledge.addWarpPerm(name, amount);
                }
            }
        }
    }

    @Inject(method = "addStickyWarpToPlayer", at = @At("HEAD"), remap = false, cancellable = true)
    private static void cancelFakePlayersSticky(EntityPlayer player, int amount, CallbackInfo ci) {
        if (!(player instanceof EntityPlayerMP) || player instanceof FakePlayer) {
            ci.cancel();
            final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
            final String name = player.getCommandSenderName();
            if (name != null && knowledge != null) {
                knowledge.addWarpSticky(name, amount);
            }
        }
    }
}
