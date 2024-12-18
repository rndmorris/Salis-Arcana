package dev.rndmorris.tfixins.mixins.late.world.biomes.magicalforest;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenMagicalForest;

@Mixin(value = BiomeGenMagicalForest.class, remap = false)
public abstract class MixinSkyColor extends BiomeGenBase {

    public MixinSkyColor(int biomeId) {
        super(biomeId);
    }

    @Override
    public int getSkyColorByTemp(float temp) {
        return FixinsConfig.biomeColorModule.magicalForest.skyColor.getColorValue();
    }

}
