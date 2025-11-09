package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import baubles.api.expanded.BaubleExpandedSlots;
import baubles.api.expanded.IBaubleExpanded;
import dev.rndmorris.salisarcana.common.compat.BaublesExpandedCompat;
import thaumcraft.common.items.wands.ItemFocusPouchBauble;

@Mixin(ItemFocusPouchBauble.class)
public abstract class MixinItemFocusPouchBauble_ExpandedBaublesSlot implements IBaubleExpanded {

    @Override
    public String[] getBaubleTypes(ItemStack itemStack) {
        return new String[] { BaubleExpandedSlots.beltType, BaublesExpandedCompat.POUCH_SLOT };
    }
}
