package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(ItemFocusTrade.class)
public class MixinItemFocusTrade_AddPotency {

    @Unique
    private final boolean sa$potencyEnabled = ConfigModuleRoot.enhancements.potencyModifiesHarvestLevel.isEnabled();

    @WrapMethod(method = "getPossibleUpgradesByRank", remap = false)
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank,
        Operation<FocusUpgradeType[]> original) {
        FocusUpgradeType[] original_return = original.call(itemstack, rank);
        if (!this.sa$potencyEnabled) {
            return original_return;
        }
        FocusUpgradeType[] copy = new FocusUpgradeType[original_return.length + 1];
        System.arraycopy(original_return, 0, copy, 0, original_return.length);
        copy[original_return.length] = FocusUpgradeType.potency;
        return copy;
    }
}
