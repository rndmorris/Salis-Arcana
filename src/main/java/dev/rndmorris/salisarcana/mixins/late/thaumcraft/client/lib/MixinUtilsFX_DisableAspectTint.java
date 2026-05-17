package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;

@Mixin(value = UtilsFX.class, remap = false)
public abstract class MixinUtilsFX_DisableAspectTint {

    @WrapOperation(
        method = "drawTag(DDLthaumcraft/api/aspects/Aspect;FIDIFZ)V",
        at = @At(value = "INVOKE", target = "Lthaumcraft/api/aspects/Aspect;getColor()I"))
    private static int salisarcana$disableAspectTint(Aspect instance, Operation<Integer> original) {
        // Return white such that the tint is effectively disabled.
        return 0xFFFFFF;
    }

}
