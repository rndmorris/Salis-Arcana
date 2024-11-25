package dev.rndmorris.tfixins.mixins.late.biomes;

import static dev.rndmorris.tfixins.config.FixinsConfig.eldritchBiomeColors;
import static dev.rndmorris.tfixins.config.FixinsConfig.eldritchBiomeColors;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;

import thaumcraft.common.lib.world.biomes.BiomeGenEldritch;

@Mixin(BiomeGenEldritch.class)
public abstract class MixinBiomeGenEldritch extends BiomeGenBase {

    public MixinBiomeGenEldritch(int p_i1971_1_) {
        super(p_i1971_1_);
    }

    @Override
    public int getBiomeGrassColor(int x, int y, int z) {
        if (eldritchBiomeColors != null && eldritchBiomeColors.grassSet) {
            return eldritchBiomeColors.grass;
        }
        return super.getBiomeGrassColor(x, y, z);
    }

    @Override
    public int getBiomeFoliageColor(int x, int y, int z) {
        if (eldritchBiomeColors != null && eldritchBiomeColors.foliageSet) {
            return eldritchBiomeColors.foliage;
        }
        return super.getBiomeFoliageColor(x, y, z);
    }

    @Override
    public int getSkyColorByTemp(float temp) {
        if (eldritchBiomeColors != null && eldritchBiomeColors.skySet) {
            return eldritchBiomeColors.sky;
        }
        return super.getSkyColorByTemp(temp);
    }

    @Override
    public int getWaterColorMultiplier() {
        if (eldritchBiomeColors != null && eldritchBiomeColors.waterSet) {
            return eldritchBiomeColors.water;
        }
        return super.getWaterColorMultiplier();
    }
}
