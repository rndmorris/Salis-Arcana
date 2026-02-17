package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;

@Mixin(value = UtilsFX.class, remap = false)
public class MixinUtilsFX_DisableAspectTint {

    @Redirect(
        method="drawTag(DDLthaumcraft/api/aspects/Aspect;FIDIFZ)V",
        at = @At(
            value = "INVOKE",
            target="Lthaumcraft/api/aspects/Aspect;getColor()I"
        )
    )
    private static int salisarcana$disableAspectTint(Aspect instance) {
        return 16777215;
    }


}
