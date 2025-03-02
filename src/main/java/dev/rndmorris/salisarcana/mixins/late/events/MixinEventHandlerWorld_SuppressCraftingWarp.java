package dev.rndmorris.salisarcana.mixins.late.events;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.SalisArcana;
import dev.rndmorris.salisarcana.common.interfaces.ISuppressCraftingWarp;
import thaumcraft.common.lib.events.EventHandlerWorld;

@Mixin(value = EventHandlerWorld.class, remap = false)
public class MixinEventHandlerWorld_SuppressCraftingWarp implements ISuppressCraftingWarp {

    @Unique
    private int sa$skipNextCraftingWarp = 0;

    @WrapOperation(
        method = "onCrafting",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/Thaumcraft;addStickyWarpToPlayer(Lnet/minecraft/entity/player/EntityPlayer;I)V"))
    private void wrapAddCraftingWarp(EntityPlayer player, int amount, Operation<Void> original) {
        if (sa$skipNextCraftingWarp != 0) {
            sa$skipNextCraftingWarp -= sa$skipNextCraftingWarp > 0 ? 1 : 0;
            return;
        }
        original.call(player, amount);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void skipNextCraftingWarp() {
        if (!SalisArcana.proxy.isServerThread()) {
            return;
        }
        if (sa$skipNextCraftingWarp >= 0) {
            sa$skipNextCraftingWarp += 1;
        }
    }
}
