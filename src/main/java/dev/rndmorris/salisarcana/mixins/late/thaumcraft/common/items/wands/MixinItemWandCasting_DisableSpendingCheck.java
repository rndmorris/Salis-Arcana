package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class)
public class MixinItemWandCasting_DisableSpendingCheck {

    @ModifyExpressionValue(
        method = "consumeAllVis",
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z", remap = true),
        remap = false)
    private boolean isRemoteOverride(boolean isRemote) {
        return false;
    }
}
