package dev.rndmorris.salisarcana.mixins.late.lib;

import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

@Mixin(ThaumcraftCraftingManager.class)
public class MixinThaumcraftCraftingManager {

    @ModifyExpressionValue(
        method = "getAspectsFromIngredients",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getContainerItem()Lnet/minecraft/item/Item;",
            ordinal = 1))
    private static Item getContainerItem(Item original) {
        return null;
    }
}
