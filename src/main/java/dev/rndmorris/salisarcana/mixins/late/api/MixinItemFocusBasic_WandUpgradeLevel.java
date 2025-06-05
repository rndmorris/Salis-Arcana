package dev.rndmorris.salisarcana.mixins.late.api;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(ItemFocusBasic.class)
public class MixinItemFocusBasic_WandUpgradeLevel {

    @WrapMethod(method = "getAppliedUpgrades", remap = false)
    public short[] unwrapWandItem(ItemStack focusStack, Operation<short[]> original) {
        if (focusStack.getItem() instanceof ItemWandCasting wand) {
            focusStack = wand.getFocusItem(focusStack);
        }

        return original.call(focusStack);
    }
}
