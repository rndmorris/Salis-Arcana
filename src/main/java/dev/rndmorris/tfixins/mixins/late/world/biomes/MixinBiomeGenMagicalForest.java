package dev.rndmorris.tfixins.mixins.late.world.biomes;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenMagicalForest;

@Mixin(BiomeGenMagicalForest.class)
public abstract class MixinBiomeGenMagicalForest extends BiomeGenBase {

    public MixinBiomeGenMagicalForest(int p_i1971_1_) {
        super(p_i1971_1_);
    }

    @Inject(method = "getBiomeGrassColor", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetBiomeGrassColor(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        final var magicalForestBiomeColors = FixinsConfig.biomeColorModule.magicalForestBiomeColorsSettings;
        if (magicalForestBiomeColors.grassSet) {
            cir.setReturnValue(magicalForestBiomeColors.grass);
            cir.cancel();
        }
    }

    @Inject(method = "getBiomeFoliageColor", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetBiomeFoliageColor(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        final var magicalForestBiomeColors = FixinsConfig.biomeColorModule.magicalForestBiomeColorsSettings;
        if (magicalForestBiomeColors.foliageSet) {
            cir.setReturnValue(magicalForestBiomeColors.foliage);
            cir.cancel();
        }
    }

    @Override
    public int getSkyColorByTemp(float temp) {
        final var magicalForestBiomeColors = FixinsConfig.biomeColorModule.magicalForestBiomeColorsSettings;
        if (magicalForestBiomeColors.skySet) {
            return magicalForestBiomeColors.sky;
        }
        return super.getSkyColorByTemp(temp);
    }

    @Inject(method = "getWaterColorMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetWaterColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        final var magicalForestBiomeColors = FixinsConfig.biomeColorModule.magicalForestBiomeColorsSettings;
        if (magicalForestBiomeColors.waterSet) {
            cir.setReturnValue(magicalForestBiomeColors.water);
            cir.cancel();
        }
    }
}
