package dev.rndmorris.tfixins.mixins.late.world.biomes.eldritch;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;

import dev.rndmorris.tfixins.config.ConfigModuleRoot;
import thaumcraft.common.lib.world.biomes.BiomeGenEldritch;

@Mixin(value = BiomeGenEldritch.class, remap = false)
public abstract class MixinGrassColor extends BiomeGenBase {

    public MixinGrassColor(int biomeId) {
        super(biomeId);
    }

    @Override
    public int getBiomeGrassColor(int x, int y, int z) {
        return ConfigModuleRoot.biomeColorModule.eldritch.grassColor.getColorValue();
    }

}
