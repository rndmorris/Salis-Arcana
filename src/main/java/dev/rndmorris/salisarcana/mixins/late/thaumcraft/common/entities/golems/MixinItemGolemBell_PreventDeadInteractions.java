package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.golems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.entities.golems.ItemGolemBell;

@Mixin(ItemGolemBell.class)
public abstract class MixinItemGolemBell_PreventDeadInteractions {

    @WrapMethod(method = "onLeftClickEntity", remap = false)
    private boolean preventLeftClicks(ItemStack stack, EntityPlayer player, Entity entity,
        Operation<Boolean> original) {
        if (entity instanceof EntityLivingBase base && base.getHealth() <= 0.0f) return false;
        return original.call(stack, player, entity);
    }

    @WrapMethod(method = "itemInteractionForEntity")
    private boolean preventRightClicks(ItemStack stack, EntityPlayer player, EntityLivingBase target,
        Operation<Boolean> original) {
        if (target.isDead || target.getHealth() <= 0.0f) return false;
        return original.call(stack, player, target);
    }
}
