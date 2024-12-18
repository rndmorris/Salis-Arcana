package dev.rndmorris.tfixins.mixins.late.world.biomes.eerie;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenEerie;

@Mixin(value = BiomeGenEerie.class, remap = false)
public abstract class MixinSkyColor extends BiomeGenBase {

    public MixinSkyColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "getSkyColorByTemp", at = @At("HEAD"), cancellable = true)
    private void mixinGetSkyColorByTemp(float temp, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FixinsConfig.biomeColorModule.eerie.skyColor.getColorValue());
        cir.cancel();
    }

}
