package dev.rndmorris.salisarcana.mixins.late.items;

import javax.annotation.Nullable;

import net.minecraft.util.IIcon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.items.wands.ItemWandRod;

@Mixin(value = ItemWandRod.class, remap = false)
public abstract class MixinItemWandRod {

    @Shadow
    public IIcon[] iconWand;

    @Shadow
    public IIcon[] iconStaff;

    @Shadow
    public IIcon iconPrimalStaff;

    @Inject(method = "getIconFromDamage", at = @At("HEAD"), cancellable = true, remap = true)
    private void mixinGetIconFromDamage(int meta, CallbackInfoReturnable<IIcon> cir) {
        if (meta < 50) {
            final var icon = sa$tryGet(iconWand, meta);
            cir.setReturnValue(icon);
            cir.cancel();
            return;
        }
        if (meta < 100) {
            final var icon = sa$tryGet(iconStaff, meta - 50);
            cir.setReturnValue(icon);
            cir.cancel();
            return;
        }
        cir.setReturnValue(iconPrimalStaff);
        cir.cancel();
    }

    @Unique
    private @Nullable IIcon sa$tryGet(IIcon[] arr, int index) {
        if (0 <= index && index < arr.length) {
            return arr[index];
        }
        return null;
    }

}
