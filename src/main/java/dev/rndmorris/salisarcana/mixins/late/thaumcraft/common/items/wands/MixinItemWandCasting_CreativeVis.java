package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class, remap = false)
public class MixinItemWandCasting_CreativeVis {

    @Unique
    private static boolean sa$isCreative(EntityPlayer player) {
        return player != null && player.capabilities != null && player.capabilities.isCreativeMode;
    }

    @WrapMethod(method = "consumeVis")
    private boolean wrapConsumeVis(ItemStack is, EntityPlayer player, Aspect aspect, int amount, boolean crafting,
        Operation<Boolean> original) {
        if (sa$isCreative(player)) {
            return true;
        }
        return original.call(is, player, aspect, amount, crafting);
    }

    @WrapMethod(method = "consumeAllVis")
    private boolean wrapConsumeAllVis(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit,
        boolean crafting, Operation<Boolean> original) {
        if (sa$isCreative(player)) {
            return true;
        }
        return original.call(is, player, aspects, doit, crafting);
    }

    @WrapMethod(method = "consumeAllVisCrafting")
    private boolean wrapConsumeAllVisCrafting(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit,
        Operation<Boolean> original) {
        if (sa$isCreative(player)) {
            return true;
        }
        return original.call(is, player, aspects, doit);
    }
}
