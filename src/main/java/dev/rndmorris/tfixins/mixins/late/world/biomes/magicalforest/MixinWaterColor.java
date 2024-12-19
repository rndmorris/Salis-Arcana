package dev.rndmorris.tfixins.mixins.late.world.biomes.magicalforest;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.rndmorris.tfixins.config.ModuleRoot;
import thaumcraft.common.lib.world.biomes.BiomeGenMagicalForest;

@Mixin(value = BiomeGenMagicalForest.class, remap = false)
public abstract class MixinWaterColor extends BiomeGenBase {

    public MixinWaterColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "getWaterColorMultiplier", at = @At("HEAD"), cancellable = true)
    private void mixinGetWaterColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ModuleRoot.biomeColorModule.magicalForest.waterColorMultiplier.getColorValue());
        cir.cancel();
    }

}
