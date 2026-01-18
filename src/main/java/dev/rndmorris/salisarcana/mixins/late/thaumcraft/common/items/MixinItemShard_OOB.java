package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.items.ItemShard;

@Mixin(ItemShard.class)
public class MixinItemShard_OOB extends Item {

    @WrapMethod(method = "getColorFromItemStack")
    public int preventOOBColor(ItemStack stack, int par2, Operation<Integer> original) {
        return stack.getItemDamage() > 6 ? 0 : original.call(stack, par2);
    }
}
