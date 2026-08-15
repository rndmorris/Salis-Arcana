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

    /**
     * The original implementation calculates the new total as a {@code float} then truncates to an {@code int}
     * for every aspect is touches. To avoid truncating I instead keep the new running total as a {@code float}.
     * I skipped multiplying by {@code 100.0F} here because that can be factored out to the very end.
     */
    @Definition(id = "tot", local = @Local(name = "tot", type = int.class))
    @Definition(id = "mod", local = @Local(name = "mod", type = float.class))
    @Expression("(float)tot + mod * 100.0")
    @ModifyExpressionValue(method = "addInformation", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float trackTotAsFloat(float original, @Local(name = "mod") float mod, @Share("totF") LocalFloatRef totF) {
        totF.set(totF.get() + mod);
        return original;
    }

    /**
     * Round our average to the nearest whole number for display purposes. This lets thaumium+silverwood
     * scepters (actual value 79.something) be rounded up to 80.
     */
    @Definition(id = "tot", local = @Local(name = "tot", type = int.class))
    @Definition(id = "num", local = @Local(name = "num", type = int.class))
    @Expression("tot / num")
    @ModifyExpressionValue(method = "addInformation", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int useRoundedAverage(int original, @Share("totF") LocalFloatRef totF, @Local(name = "num") int num) {
        return Math.round(totF.get() / num * 100);
    }
}
