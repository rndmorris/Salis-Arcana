package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public class MixinItemWandCasting {

    @Inject(method = "consumeVis", at = @At(value = "HEAD"), cancellable = true)
    private void consumeVisOverride(ItemStack is, EntityPlayer player, Aspect aspect, int amount, boolean crafting,
        CallbackInfoReturnable<Boolean> cir) {
        if (player.capabilities.isCreativeMode) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "consumeAllVis", at = @At(value = "HEAD"), cancellable = true)
    private void consumeAllVisOverride(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit,
        boolean crafting, CallbackInfoReturnable<Boolean> cir) {
        if (player.capabilities.isCreativeMode) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "consumeAllVisCrafting", at = @At(value = "HEAD"), cancellable = true)
    private void consumeVisOverride(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit,
        CallbackInfoReturnable<Boolean> cir) {
        if (player.capabilities.isCreativeMode) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
