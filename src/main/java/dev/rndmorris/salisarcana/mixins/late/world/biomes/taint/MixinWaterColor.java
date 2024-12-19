package dev.rndmorris.salisarcana.mixins.late.world.biomes.taint;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.lib.world.biomes.BiomeGenTaint;

@Mixin(value = BiomeGenTaint.class, remap = false)
public abstract class MixinWaterColor extends BiomeGenBase {

    public MixinWaterColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "getWaterColorMultiplier", at = @At("HEAD"), cancellable = true)
    private void mixinGetWaterColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ConfigModuleRoot.biomeColors.taint.waterColorMultiplier.getColorValue());
        cir.cancel();
    }

}
