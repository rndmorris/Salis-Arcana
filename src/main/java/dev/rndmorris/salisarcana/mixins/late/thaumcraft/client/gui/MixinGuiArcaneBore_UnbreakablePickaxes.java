package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.client.gui.GuiArcaneBore;

@Mixin(GuiArcaneBore.class)
abstract class MixinGuiArcaneBore_UnbreakablePickaxes {

    @ModifyExpressionValue(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxDamage()I"))
    private int ignoreUnbreakablePickaxe(int maxDamage) {
        // Some unbreakable tools like those in Thaumic Tinkerer use negative values
        return maxDamage <= 0 ? Integer.MAX_VALUE : maxDamage;
    }
}
