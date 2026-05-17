package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.baubles.ItemAmuletVis;

@Mixin(value = ItemAmuletVis.class, remap = false)
public abstract class MixinItemAmuletVis_TickRate {

    @Unique
    private final int sa$tickRate = SalisConfig.features.visAmuletTickRate.getValue();

    @ModifyConstant(method = "onWornTick", constant = @Constant(intValue = 5, ordinal = 0))
    private int modifyTickRate(int original) {
        return sa$tickRate;
    }
}
