package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public abstract class MixinItemWandCasting_NamedStaffters {

    @Shadow
    public abstract boolean isStaff(ItemStack stack);

    @Shadow
    public abstract boolean isSceptre(ItemStack stack);

    @ModifyVariable(
        method = "getItemStackDisplayName",
        ordinal = 0,
        at = @At(value = "STORE", ordinal = 1),
        remap = true)
    public String addStaffterTranslation(String name, @Local(argsOnly = true) ItemStack stack) {
        if (this.isStaff(stack) && this.isSceptre(stack)) {
            return name.replace("%OBJ", StatCollector.translateToLocal("item.Wand.staffter.obj"));
        } else {
            return name;
        }
    }
}
