package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.gui;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import thaumcraft.client.gui.GuiArcaneBore;

@Mixin(GuiArcaneBore.class)
abstract class MixinGuiArcaneBore_UnbreakablePickaxes {

    @Redirect(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxDamage()I"))
    private int ignoreUnbreakablePickaxe(ItemStack stack) {
        int maxDamage = stack.getMaxDamage();
        // Some unbreakable tools like those in Thaumic Tinkerer use negative values
        return maxDamage <= 0 ? Integer.MAX_VALUE : maxDamage;
    }
}
