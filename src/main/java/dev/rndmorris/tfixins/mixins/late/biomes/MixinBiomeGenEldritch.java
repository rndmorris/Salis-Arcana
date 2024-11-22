package dev.rndmorris.tfixins.mixins.late.biomes;

import static dev.rndmorris.tfixins.Config.eerieBiomeColors;

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
        if (eerieBiomeColors != null && eerieBiomeColors.grassSet) {
            return eerieBiomeColors.grass;
        }
        return super.getBiomeGrassColor(x, y, z);
    }

    @Override
    public int getBiomeFoliageColor(int x, int y, int z) {
        if (eerieBiomeColors != null && eerieBiomeColors.foliageSet) {
            return eerieBiomeColors.foliage;
        }
        return super.getBiomeFoliageColor(x, y, z);
    }

    @Override
    public int getSkyColorByTemp(float temp) {
        if (eerieBiomeColors != null && eerieBiomeColors.skySet) {
            return eerieBiomeColors.sky;
        }
        return super.getSkyColorByTemp(temp);
    }

    @Override
    public int getWaterColorMultiplier() {
        if (eerieBiomeColors != null && eerieBiomeColors.waterSet) {
            return eerieBiomeColors.water;
        }
        return super.getWaterColorMultiplier();
    }
}
