package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.lib.events.KeyHandler;

@Mixin(value = KeyHandler.class, remap = false)
abstract class MixinKeyHandler_LocalizeKeybinds {

    @ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "stringValue=Change Wand Focus"))
    private String localizeF(String original) {
        return "tc.key.change_wand_focus";
    }

    @ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "stringValue=Activate Hover Harness"))
    private String localizeH(String original) {
        return "tc.key.hover_harness";
    }

    @ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "stringValue=Misc Wand Toggle"))
    private String localizeG(String original) {
        return "tc.key.wand_toggle";
    }
}
