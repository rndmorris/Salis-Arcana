package dev.rndmorris.tfixins.mixins.late.world.biomes.eldritch;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.tfixins.config.FixinsConfig;
import thaumcraft.common.lib.world.biomes.BiomeGenEldritch;

@Mixin(value = BiomeGenEldritch.class, remap = false)
public abstract class MixinWaterColor extends BiomeGenBase {

    public MixinWaterColor(int biomeId) {
        super(biomeId);
    }

    @Override
    public int getWaterColorMultiplier() {
        return FixinsConfig.biomeColorModule.eldritch.waterColorMultiplier.getColorValue();
    }

}
