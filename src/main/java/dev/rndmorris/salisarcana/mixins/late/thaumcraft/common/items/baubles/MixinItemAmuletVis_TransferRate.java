package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.baubles.ItemAmuletVis;

@Mixin(value = ItemAmuletVis.class, remap = false)
public abstract class MixinItemAmuletVis_TransferRate {

    @Unique
    private final int sa$transferRate = SalisConfig.features.visAmuletTransferRate.getValue();

    @ModifyConstant(
        method = "onWornTick",
        constant = @Constant(intValue = 5),
        // we don't want to modify the first constant, so we have to slice it out of the
        // method.
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/EntityLivingBase;getHeldItem()Lnet/minecraft/item/ItemStack;")))
    private int modifyTransferRate(int original) {
        return sa$transferRate;
    }
}
