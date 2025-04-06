package dev.rndmorris.salisarcana.mixins.late.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import dev.rndmorris.salisarcana.config.Config;
import thaumcraft.common.blocks.BlockManaPod;

@Mixin(value = BlockManaPod.class)
public class MixinBlockManaPod {

    @ModifyConstant(method = "updateTick", constant = @Constant(intValue = 30))
    private int growthChance(int original) {
        return Config.enhancements.manaPodGrowthRate.getValue();
    }
}
