package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public abstract class MixinRenderEventHandler_DetectZeroAspectBlocks {

    @ModifyExpressionValue(
        method = "startScan",
        at = @At(value = "INVOKE", target = "Lthaumcraft/api/aspects/AspectList;visSize()I"))
    private int showZeroAspectOres(int original) {
        // anything that returns 0 is effectively
        // excluded from the scan, and we can't have that
        return Math.max(1, original);
    }

}
