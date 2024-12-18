package dev.rndmorris.tfixins.mixins.late.world.biomes.eerie;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenEerie;

@Mixin(value = BiomeGenEerie.class, remap = false)
public abstract class MixinBaseColor extends BiomeGenBase {

    public MixinBaseColor(int biomeId) {
        super(biomeId);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void mixinInit(int biomeId, CallbackInfo ci) {
        setColor(FixinsConfig.biomeColorModule.eerie.baseColor.getColorValue());
    }

}
