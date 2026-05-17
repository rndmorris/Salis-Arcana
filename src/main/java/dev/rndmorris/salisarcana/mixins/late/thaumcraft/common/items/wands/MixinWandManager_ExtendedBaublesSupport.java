package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

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

@Mixin(WandManager.class)
public abstract class MixinWandManager_ExtendedBaublesSupport {

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

    @ModifyConstant(method = "changeFocus", constant = @Constant(intValue = 4), remap = false)
    private static int changeFocusAllBaubles(int value, ItemStack is, World w, EntityPlayer player, String focus) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }

    @ModifyConstant(
        method = { "fetchFocusFromPouch", "addFocusToPouch" },
        constant = @Constant(intValue = 4),
        remap = false)
    private static int fetchFocusAllBaubles(int value, EntityPlayer player) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }
}
