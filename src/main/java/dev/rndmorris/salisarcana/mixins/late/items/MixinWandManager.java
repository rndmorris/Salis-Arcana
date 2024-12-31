package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import baubles.api.BaublesApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.WandManager;

@Mixin(value = WandManager.class, priority = 1001)
public abstract class MixinWandManager {

    @ModifyConstant(method = "getTotalVisDiscount", constant = @Constant(intValue = 4, ordinal = 0), remap = false)
    private static int getTotalVisDiscountWithAllBaubles(int value, EntityPlayer player, Aspect aspect) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }

    @ModifyConstant(method = "consumeVisFromInventory", constant = @Constant(intValue = 4, ordinal = 0), remap = false)
    private static int consumeVisFromBaublesInventory(int value, EntityPlayer player, AspectList cost) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }

    @ModifyConstant(method = "changeFocus", constant = @Constant(intValue = 4, ordinal = 0), remap = false)
    private static int changeFocusAllBaubles(int value, ItemStack is, World w, EntityPlayer player, String focus) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }

}
