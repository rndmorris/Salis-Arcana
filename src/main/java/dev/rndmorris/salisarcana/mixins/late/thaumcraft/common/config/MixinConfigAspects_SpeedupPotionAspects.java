package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.config;

import java.util.List;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.PotionMetadataCache;
import thaumcraft.common.config.ConfigAspects;

@Mixin(value = ConfigAspects.class, remap = false)
abstract class MixinConfigAspects_SpeedupPotionAspects {

    @Unique
    private static PotionMetadataCache<List<PotionEffect>> salisarcana$potionEffectCache;

    @Inject(method = "registerItemAspects", at = @At("HEAD"), require = 1)
    private static void salisarcana$createPotionEffectCache(CallbackInfo ci) {
        int metadataMask = PotionMetadataCache
            .findRelevantBits(PotionHelper.potionRequirements.values(), PotionHelper.potionAmplifiers.values());

        salisarcana$potionEffectCache = metadataMask == PotionMetadataCache.ALL_METADATA_BITS ? null
            : new PotionMetadataCache<>(metadataMask);
    }

    @WrapOperation(
        method = "registerItemAspects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/potion/PotionHelper;getPotionEffects(IZ)Ljava/util/List;",
            remap = true),
        require = 1)
    private static List<PotionEffect> salisarcana$cachePotionEffects(int metadata, boolean includeUsable,
        Operation<List<PotionEffect>> original) {
        if (salisarcana$potionEffectCache == null) return original.call(metadata, includeUsable);
        return salisarcana$potionEffectCache.get(metadata, value -> original.call(value, includeUsable));
    }

    @Inject(method = "registerItemAspects", at = @At("RETURN"), require = 1)
    private static void salisarcana$clearPotionEffectCache(CallbackInfo ci) {
        salisarcana$potionEffectCache = null;
    }
}
