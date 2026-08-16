package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import net.minecraft.client.settings.KeyBinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.client.QuickStashFocus;
import thaumcraft.common.lib.events.KeyHandler;

@Mixin(KeyHandler.class)
abstract class MixinKeyHandler_CaptureFocusKeybind {

    @Shadow
    public KeyBinding keyF;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureKeys(CallbackInfo ci) {
        if (QuickStashFocus.KEYBIND == null) {
            QuickStashFocus.KEYBIND = this.keyF;
        }
    }
}
