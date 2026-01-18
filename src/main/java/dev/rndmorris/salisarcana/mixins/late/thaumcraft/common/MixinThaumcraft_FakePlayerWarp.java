package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.Thaumcraft;

@Mixin(Thaumcraft.class)
public class MixinThaumcraft_FakePlayerWarp {

    @WrapMethod(method = "addWarpToPlayer", remap = false)
    private static void cancelFakePlayers(EntityPlayer player, int amount, boolean temporary,
        Operation<Void> original) {
        if (!(player instanceof EntityPlayerMP) || player instanceof FakePlayer) {
            final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
            final String name = player.getCommandSenderName();
            if (name != null && knowledge != null) {
                if (temporary) {
                    knowledge.addWarpTemp(name, amount);
                } else if (amount > 0) {
                    knowledge.addWarpPerm(name, amount);
                }
            }
        } else {
            original.call(player, amount, temporary);
        }
    }

    @WrapMethod(method = "addStickyWarpToPlayer", remap = false)
    private static void cancelFakePlayersSticky(EntityPlayer player, int amount, Operation<Void> original) {
        if (!(player instanceof EntityPlayerMP) || player instanceof FakePlayer) {
            final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
            final String name = player.getCommandSenderName();
            if (name != null && knowledge != null) {
                knowledge.addWarpSticky(name, amount);
            }
        } else {
            original.call(player, amount);
        }
    }
}
