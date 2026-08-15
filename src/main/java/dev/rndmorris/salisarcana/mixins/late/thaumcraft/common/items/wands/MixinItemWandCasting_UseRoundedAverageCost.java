package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;

import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(ItemWandCasting.class)
abstract class MixinItemWandCasting_UseRoundedAverageCost {

    @Definition(id = "tot", local = @Local(name = "tot", type = int.class))
    @Definition(id = "mod", local = @Local(name = "mod", type = float.class))
    @Expression("(float)tot + mod * 100.0")
    @ModifyExpressionValue(method = "addInformation", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float trackTotAsFloat(float original, @Local(name = "mod") float mod, @Share("totF") LocalFloatRef totF) {
        totF.set(totF.get() + mod);
        return original;
    }

    @Definition(id = "tot", local = @Local(name = "tot", type = int.class))
    @Definition(id = "num", local = @Local(name = "num", type = int.class))
    @Expression("tot / num")
    @ModifyExpressionValue(method = "addInformation", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int useRoundedAverage(int original, @Share("totF") LocalFloatRef totF, @Local(name = "num") int num) {
        return Math.round(totF.get() / num * 100);
    }
}
