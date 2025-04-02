package dev.rndmorris.salisarcana.mixins.late.items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.items.baubles.ItemAmuletVis;

@Mixin(ItemAmuletVis.class)
public abstract class MixinItemAmuletVis_TickRate {

    @Unique
    private final int sa$tickRate = ConfigModuleRoot.enhancements.visAmuletTickRate.getValue();

    @ModifyConstant(method = "onWornTick", constant = @Constant(intValue = 5, ordinal = 0))
    private int modifyTickRate(int original) {
        return sa$tickRate;
    }
}
