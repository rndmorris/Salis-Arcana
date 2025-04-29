package dev.rndmorris.salisarcana.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.blocks.BlockMagicalLogItem;

@Mixin(BlockMagicalLogItem.class)
public class MixinBlockMagicalLogItem {

    @ModifyExpressionValue(
        method = "getUnlocalizedName",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItemDamage()I"))
    public int limitToValidMetadata(int original) {
        return original & 3;
    }
}
