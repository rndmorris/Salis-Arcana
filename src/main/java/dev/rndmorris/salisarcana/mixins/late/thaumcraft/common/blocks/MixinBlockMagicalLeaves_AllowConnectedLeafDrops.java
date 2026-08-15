package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.blocks.BlockMagicalLeaves;

@Mixin(BlockMagicalLeaves.class)
abstract class MixinBlockMagicalLeaves_AllowConnectedLeafDrops {

    @Expression("(? & 8) != 0")
    @ModifyExpressionValue(method = "dropBlockAsItemWithChance", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean skipDecayCheck(boolean original) {
        return true;
    }
}
