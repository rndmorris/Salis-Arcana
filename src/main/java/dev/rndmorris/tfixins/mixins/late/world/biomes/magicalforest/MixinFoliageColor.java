package dev.rndmorris.tfixins.mixins.late.world.biomes.magicalforest;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenMagicalForest;

@Mixin(value = BiomeGenMagicalForest.class, remap = false)
public abstract class MixinFoliageColor extends BiomeGenBase {

    public MixinFoliageColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "getBiomeFoliageColor", at = @At("HEAD"), cancellable = true)
    private void mixinGetBiomeFoliageColor(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FixinsConfig.biomeColorModule.magicalForest.foliageColor.getColorValue());
        cir.cancel();
    }

}
