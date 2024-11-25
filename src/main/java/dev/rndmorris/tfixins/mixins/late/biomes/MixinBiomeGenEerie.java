package dev.rndmorris.tfixins.mixins.late.biomes;

import static dev.rndmorris.tfixins.Config.eerieBiomeColors;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.lib.world.biomes.BiomeGenEerie;

@Mixin(BiomeGenEerie.class)
public abstract class MixinBiomeGenEerie extends BiomeGenBase {

    public MixinBiomeGenEerie(int p_i1971_1_) {
        super(p_i1971_1_);
    }

    @Inject(method = "getBiomeGrassColor", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetBiomeGrassColor(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (eerieBiomeColors != null && eerieBiomeColors.grassSet) {
            cir.setReturnValue(eerieBiomeColors.grass);
            cir.cancel();
        }
    }

    @Inject(method = "getBiomeFoliageColor", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetBiomeFoliageColor(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (eerieBiomeColors != null && eerieBiomeColors.foliageSet) {
            cir.setReturnValue(eerieBiomeColors.foliage);
            cir.cancel();
        }
    }

    @Inject(method = "getSkyColorByTemp", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetSkyColorByTemp(float temp, CallbackInfoReturnable<Integer> cir) {
        if (eerieBiomeColors != null && eerieBiomeColors.skySet) {
            cir.setReturnValue(eerieBiomeColors.sky);
            cir.cancel();
        }
    }

    @Inject(method = "getWaterColorMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetWaterColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        if (eerieBiomeColors != null && eerieBiomeColors.waterSet) {
            cir.setReturnValue(eerieBiomeColors.water);
            cir.cancel();
        }
    }
}
