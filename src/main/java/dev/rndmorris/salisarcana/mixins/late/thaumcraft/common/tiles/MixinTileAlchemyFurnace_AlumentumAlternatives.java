package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileAlchemyFurnace;

@Mixin(value = TileAlchemyFurnace.class)
abstract class MixinTileAlchemyFurnace_EffectiveFuels extends TileThaumcraft {

    @WrapOperation(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemEqual(Lnet/minecraft/item/ItemStack;)Z"))
    boolean effectiveFuels(ItemStack instance, ItemStack p_77969_1_, Operation<Boolean> original) {
        if (SalisConfig.features.alchemicalFurnaceEffectiveFuels
            .hasEntry(instance.getItem(), instance.getItemDamage())) {
            return true;
        }
        return original.call(instance, p_77969_1_);
    }
}
