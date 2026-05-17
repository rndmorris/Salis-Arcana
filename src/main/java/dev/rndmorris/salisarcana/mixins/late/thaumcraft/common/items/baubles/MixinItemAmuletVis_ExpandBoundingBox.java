package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.baubles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.items.baubles.ItemAmuletVis;

@Mixin(value = ItemAmuletVis.class, remap = false)
public class MixinItemAmuletVis_ExpandBoundingBox {

    @Unique
    private static final double sa$Expanded = SalisConfig.features.visRelayBoxExpansion.getValue();

    @ModifyConstant(method = "onWornTick", constant = @Constant(doubleValue = 26.0D))
    private double modifyBoundingBox(double original) {
        return sa$Expanded * sa$Expanded + 1.0D;
    }
}
