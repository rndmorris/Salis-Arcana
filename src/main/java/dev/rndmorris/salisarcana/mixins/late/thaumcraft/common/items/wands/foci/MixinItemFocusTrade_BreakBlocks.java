package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(value = ItemFocusTrade.class, remap = false)
public class MixinItemFocusTrade_BreakBlocks extends ItemFocusBasic {

    @WrapMethod(method = "func_150893_a") // getStrVsBlock
    public float fixGetStrVsBlock(ItemStack itemstack, Block block, Operation<Float> original) {
        return itemstack.getItem() instanceof ItemWandCasting ? original.call(itemstack, block) : 1.0F;
    }
}
