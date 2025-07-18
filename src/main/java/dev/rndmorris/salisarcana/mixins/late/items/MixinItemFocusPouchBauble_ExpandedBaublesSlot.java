package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import baubles.api.BaubleType;
import baubles.api.expanded.IBaubleExpanded;
import dev.rndmorris.salisarcana.common.compat.BaublesExpandedCompat;
import thaumcraft.common.items.wands.ItemFocusPouchBauble;

@Mixin(ItemFocusPouchBauble.class)
public abstract class MixinItemFocusPouchBauble_ExpandedBaublesSlot implements IBaubleExpanded {

    @Shadow
    public abstract BaubleType getBaubleType(ItemStack itemstack);

    @Override
    public String[] getBaubleTypes(ItemStack itemStack) {
        return new String[] { BaublesExpandedCompat.POUCH_SLOT };
    }
}
