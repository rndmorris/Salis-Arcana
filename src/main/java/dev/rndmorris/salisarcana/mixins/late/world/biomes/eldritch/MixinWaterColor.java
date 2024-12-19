package dev.rndmorris.salisarcana.mixins.late.world.biomes.eldritch;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.lib.world.biomes.BiomeGenEldritch;

@Mixin(value = BiomeGenEldritch.class, remap = false)
public abstract class MixinWaterColor extends BiomeGenBase {

    public MixinWaterColor(int biomeId) {
        super(biomeId);
    }

    @Override
    public int getWaterColorMultiplier() {
        return ConfigModuleRoot.biomeColors.eldritch.waterColorMultiplier.getColorValue();
    }

}
