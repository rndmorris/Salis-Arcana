package dev.rndmorris.salisarcana.mixins.early.gui;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.client.handlers.SalisContainerInputHandler;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorGuiContainer;

@Mixin(GuiContainer.class)
abstract class MixinGuiContainer_HandleKeyTyped implements AccessorGuiContainer {

    @Inject(method = "keyTyped", at = @At("TAIL"))
    private void handleKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        SalisContainerInputHandler.INSTANCE.lastKeyTyped((GuiContainer) (Object) this, typedChar, keyCode);
    }
}
