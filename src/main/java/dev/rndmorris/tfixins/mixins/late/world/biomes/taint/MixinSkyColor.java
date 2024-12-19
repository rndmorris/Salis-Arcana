package dev.rndmorris.tfixins.mixins.late.world.biomes.taint;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import thaumcraft.common.lib.world.biomes.BiomeGenTaint;

@Mixin(value = BiomeGenTaint.class, remap = false)
public abstract class MixinSkyColor extends BiomeGenBase {

    public MixinSkyColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "getSkyColorByTemp", at = @At("HEAD"), cancellable = true)
    private void mixinGetSkyColorByTemp(float temp, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ConfigModuleRoot.biomeColorModule.taint.skyColor.getColorValue());
        cir.cancel();
    }

}
