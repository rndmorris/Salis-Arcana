package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(ItemFocusTrade.class)
public class MixinItemFocusTrade_AddPotency {

    @WrapMethod(method = "getPossibleUpgradesByRank", remap = false)
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank,
        Operation<FocusUpgradeType[]> original) {
        FocusUpgradeType[] original_return = original.call(itemstack, rank);
        FocusUpgradeType[] copy = new FocusUpgradeType[original_return.length + 1];
        System.arraycopy(original_return, 0, copy, 0, original_return.length);
        copy[original_return.length] = FocusUpgradeType.potency;
        return copy;
    }
}
