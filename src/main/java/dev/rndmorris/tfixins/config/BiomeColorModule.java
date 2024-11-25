package dev.rndmorris.tfixins.config;

import static dev.rndmorris.tfixins.lib.IntegerHelper.tryParseHexInteger;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import javax.annotation.Nonnull;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

public class BiomeColorModule implements IConfigModule {

    public final BiomeColors eerieBiomeColors = new BiomeColors();
    public final BiomeColors eldritchBiomeColors = new BiomeColors();
    public final BiomeColors magicalForestBiomeColors = new BiomeColors();
    public final BiomeColors taintBiomeColors = new BiomeColors();

    @Override
    public boolean enabledByDefault() {
        return false;
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "biome_colors";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Override the colors of TC4's biomes.";
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        configuration.setCategoryComment(getModuleId(), getModuleComment());

        if (isBiomeOverrideEnabled(configuration, "eerie")) {
            loadBiomeColors(configuration, "eerie", eerieBiomeColors, biomeEerie);
        }
        if (isBiomeOverrideEnabled(configuration, "eldritch")) {
            loadBiomeColors(configuration, "eldritch", eldritchBiomeColors, biomeEldritchLands);
        }
        if (isBiomeOverrideEnabled(configuration, "magical forest")) {
            loadBiomeColors(configuration, "magical_forest", magicalForestBiomeColors, biomeMagicalForest);
        }
        if (isBiomeOverrideEnabled(configuration, "taint")) {
            loadBiomeColors(configuration, "taint", taintBiomeColors, biomeTaint);
        }
    }

    private boolean isBiomeOverrideEnabled(Configuration configuration, String biomeName) {
        return configuration.getBoolean(
            String.format("Override %s biome colors?", biomeName),
            getModuleId(),
            false,
            String.format("Override the colors of the %s biome", biomeName));
    }

    private void loadBiomeColors(Configuration configuration, String category, BiomeColors output, BiomeGenBase biome) {
        if (biome == null) {
            return;
        }

        category = String.format("%s_%s", getModuleId(), category);

        configuration.setCategoryComment(
            category,
            String.format(
                "Color overrides for the %s biome. Color values must be a 6-digit hexadecimal number (e.g. 0x404840)",
                biome.biomeName));

        var colorString = configuration.getString(
            "Base Color",
            category,
            "",
            String.format("Override the biome's base color. Original value: 0x%06x.", biome.color));
        var colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            output.baseSet = true;
            output.base = colorInt;
        }

        colorString = configuration.getString(
            "Grass Color",
            category,
            "",
            String.format(
                "Override the biome's grass color.  Original value: 0x%06x",
                biome.getBiomeGrassColor(0, 0, 0)));
        colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            output.grassSet = true;
            output.grass = colorInt;
        }

        colorString = configuration.getString(
            "Foliage Color",
            category,
            "",
            String.format(
                "Override the biome's foliage color.  Original value: 0x%06x",
                biome.getBiomeFoliageColor(0, 0, 0)));
        colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            output.foliageSet = true;
            output.foliage = colorInt;
        }

        colorString = configuration.getString(
            "Sky Color",
            category,
            "",
            String.format(
                "Override the biome's sky color.  Original value: 0x%06x",
                biome.getSkyColorByTemp(biome.temperature)));
        colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            output.skySet = true;
            output.sky = colorInt;
        }

        colorString = configuration.getString(
            "Water Color",
            category,
            "",
            String
                .format("Override the biome's water color.  Original value: 0x%06x", biome.getWaterColorMultiplier()));
        colorInt = tryParseHexInteger(colorString);
        if (colorInt != null) {
            output.waterSet = true;
            output.water = colorInt;
        }
    }

    public static class BiomeColors {

        public boolean baseSet = false;
        public int base = -1;

        public boolean grassSet = false;
        public int grass = -1;

        public boolean foliageSet = false;
        public int foliage = -1;

        public boolean skySet = false;
        public int sky = -1;

        public boolean waterSet = false;
        public int water = -1;

    }

}
