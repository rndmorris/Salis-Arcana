package dev.rndmorris.salisarcana.mixins.late.items;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public class MixinItemWandCasting_DisableSpendingCheck {
    @ModifyExpressionValue(method = "consumeAllVis", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z"))
    private boolean isRemoteOverride(boolean isRemote) {
        return false;
    }
}
