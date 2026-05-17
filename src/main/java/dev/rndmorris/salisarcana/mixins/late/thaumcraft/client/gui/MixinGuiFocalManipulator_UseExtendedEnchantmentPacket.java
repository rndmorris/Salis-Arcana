package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import net.minecraft.client.multiplayer.PlayerControllerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.network.MessageExtendedEnchantItem;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.client.gui.GuiFocalManipulator;

@Mixin(GuiFocalManipulator.class)
public class MixinGuiFocalManipulator_UseExtendedEnchantmentPacket {

    @WrapOperation(
        method = "mouseClicked",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendEnchantPacket(II)V"))
    public void sendExtendedPacket(PlayerControllerMP controller, int windowID, int button, Operation<Void> original) {
        NetworkHandler.instance.sendToServer(new MessageExtendedEnchantItem((short) button));
    }
}
