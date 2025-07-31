package dev.rndmorris.salisarcana.mixins.late.config;

import net.minecraft.potion.Potion;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.IntSetting;
import dev.rndmorris.salisarcana.core.SalisArcanaCore;
import dev.rndmorris.salisarcana.lib.ArrayHelper;
import thaumcraft.common.config.Config;

@Mixin(value = Config.class, remap = false)
public abstract class MixinConfig_PotionIds {

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 0))
    private static void bypassRangeGuard(Logger instance, String s, Operation<Void> original,
        @Local(name = "customPotions") int customPotions, @Local(name = "potionOffset") LocalIntRef potionOffsetRef,
        @Share("maxPotionId") LocalIntRef maxPotionIdRef) {
        SalisArcanaCore.LOG.info("Overriding TC's potion registration.");
        original.call(instance, s);
        final var potionOffset = potionOffsetRef.get();
        final var tweaks = SalisConfig.tweaks;
        final var maxPotionId = tweaks.maxPotionIdOverride();
        if (maxPotionId > Byte.MAX_VALUE && !tweaks.potionIdLimitRaised.isEnabled()) {
            throw new RuntimeException(
                String.format(
                    "Potion ids cannot be above %s. If this limit has been raised through another mod, set %s to `true` in the Salis Arcana potion configs.",
                    Byte.MAX_VALUE,
                    tweaks.potionIdLimitRaised.getName()));
        }

        maxPotionIdRef.set(maxPotionId);
        // do we actually need to extend the array?
        if (maxPotionId >= potionOffset) {
            // setting it to a negative value will get us past the if block on the next line
            // this specific value should also trick Thaum into creating a zero-length array
            potionOffsetRef.set(-customPotions);
        }
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V"))
    private static void overrideNewArray(Object src, int srcPos, Object dest, int destPos, int length,
        Operation<Void> original, @Local(name = "potionTypes") LocalRef<Potion[]> potionTypesRef,
        @Share("maxPotionId") LocalIntRef maxPotionIdRef) {
        var potionTypes = potionTypesRef.get();
        final var maxPotionId = maxPotionIdRef.get();

        if (potionTypes.length <= maxPotionId) {
            potionTypes = new Potion[maxPotionId + 1];
            potionTypesRef.set(potionTypes);
        }

        original.call(src, srcPos, potionTypes, destPos, ((Potion[]) src).length);
    }

    // we call original in all of these just to be polite to any other mixins that might
    // be in the chain (not that I'm expecting many, if any)

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 0))
    private static int setTaintPoisonId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.taintPoisonId,
            "Taint Poison",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 1))
    private static int setFluxFluId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(start, original, SalisConfig.tweaks.fluxFluId, "Flux Flu", lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 2))
    private static int setFluxPhageId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.fluxPhageId,
            "Flux Phage",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 3))
    private static int setUnnaturalHungerId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.unnaturalHungerId,
            "Unnatural Hunger",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 4))
    private static int setWarpWardId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.warpWardId,
            "Warp Ward",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 5))
    private static int setDeadlyGazeId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.deadlyGazeId,
            "Deadly Gaze",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 6))
    private static int setBlurredVisionId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.blurredVisionId,
            "Blurred Vision",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 7))
    private static int setSunScornedId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.sunScornedId,
            "Sun Scorned",
            lastAutoIdRef);
    }

    @WrapOperation(
        method = "initPotions",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/config/Config;getNextPotionId(I)I", ordinal = 8))
    private static int setThaumarhiaId(int start, Operation<Integer> original,
        @Share("lastAutoId") LocalIntRef lastAutoIdRef) {
        return sa$getConfiguredValueOrNextId(
            start,
            original,
            SalisConfig.tweaks.thaumarhiaId,
            "Thaumarhia",
            lastAutoIdRef);
    }

    @Unique
    private static int sa$getConfiguredValueOrNextId(int start, Operation<Integer> wrappedMethod, IntSetting setting,
        String potionName, LocalIntRef lastAutoIdRef) {
        // be polite to any other mixins (if any) that are in the chain
        wrappedMethod.call(start);
        if (setting.isEnabled()) {
            return setting.getValue();
        }
        return sa$getNextOpenId(potionName, lastAutoIdRef);
    }

    @Unique
    private static int sa$getNextOpenId(String potionName, LocalIntRef lastAutoIdRef) {
        final var potionTypes = Potion.potionTypes;
        final var length = potionTypes.length;
        final var reservedIds = SalisConfig.tweaks.getPotionIdOverrides();
        final var start = 31; // vanilla thaum, with no potions from other mods, starts at 31
        for (var index = Math.max(lastAutoIdRef.get(), start); index < length; ++index) {
            if (potionTypes[index] == null && ArrayHelper.indexOf(reservedIds, index) < 0) {
                lastAutoIdRef.set(index);
                return index;
            }
        }
        throw new RuntimeException(String.format("No available potion id found for %s.", potionName));
    }
}
