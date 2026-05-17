package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.relics;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.relics.ItemThaumometer;

@Mixin(ItemThaumometer.class)
public class MixinItemThaumometer_CustomDuration {

    @ModifyConstant(method = "getMaxItemUseDuration", constant = @Constant(intValue = 25))
    private int customDuration(int original) {
        // Original duration is 20 + 5
        // It finishes the scan when remaining duration <= 5, so we want the custom duration to be duration + 5
        return SalisConfig.features.thaumometerDuration.getValue() + 5;
    }
}
