package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import thaumcraft.client.lib.ClientTickEventsFML;

@Mixin(value = ClientTickEventsFML.class, remap = false)
public class MixinClientTickEventsFML_VisOverflow {

    @ModifyVariable(method = "renderCastingWandHud", name = "loc", at = @At("STORE"))
    private int clampVisBar(int value) {
        return Math.max(0, Math.min(value, 30));
    }
}
