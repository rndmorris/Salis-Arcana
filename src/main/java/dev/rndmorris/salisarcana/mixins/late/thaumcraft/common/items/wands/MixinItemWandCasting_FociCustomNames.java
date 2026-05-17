package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(ItemWandCasting.class)
public abstract class MixinItemWandCasting_FociCustomNames {

    @WrapOperation(
        method = "addInformation",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/api/wands/ItemFocusBasic;getItemStackDisplayName(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;"))
    private String useItemStackName(ItemFocusBasic instance, ItemStack stack, Operation<String> original) {
        if (stack.hasDisplayName()) {
            return ITALIC + stack.getDisplayName() + GREEN + " (" + original.call(instance, stack) + ")";
        } else {
            return original.call(instance, stack);
        }
    }
}
