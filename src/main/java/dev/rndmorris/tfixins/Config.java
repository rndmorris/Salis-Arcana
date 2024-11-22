package dev.rndmorris.tfixins;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import java.io.File;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String categoryBiomeEerie = "biome_eerie";
    private static final String categoryBiomeEldritch = "biome_eldritch";
    private static final String categoryBiomeMagicalForest = "biome_magical_forest";
    private static final String categoryBiomeTaint = "biome_taint";

    public static BiomeColors eerieBiomeColors = null;
    public static BiomeColors eldritchBiomeColors = null;
    public static BiomeColors magicalForestBiomeColors = null;
    public static BiomeColors taintBiomeColors = null;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        loadWorldConfigs(configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static void loadWorldConfigs(Configuration configuration) {
        eerieBiomeColors = loadBiomeColors(configuration, categoryBiomeEerie, biomeEerie);
        eldritchBiomeColors = loadBiomeColors(configuration, categoryBiomeEldritch, biomeEldritchLands);
        magicalForestBiomeColors = loadBiomeColors(configuration, categoryBiomeMagicalForest, biomeMagicalForest);
        taintBiomeColors = loadBiomeColors(configuration, categoryBiomeTaint, biomeTaint);
    }

    private static BiomeColors loadBiomeColors(Configuration configuration, String category, BiomeGenBase biome) {
        final var result = new BiomeColors();

        if (biome == null) {
            return result;
        }

        configuration.setCategoryComment(
            category,
            String.format(
                "Configuration options for the %s biome. Color values must be a 6-digit hexadecimal number (e.g. 0x404840)",
                biome.biomeName));

        final var biomeName = biome.biomeName;

        var colorString = configuration.getString(
            "Base Color",
            category,
            "",
            String.format(
                "Override the biome's base color. Original value: 0x%06x.",
                biome.color)
        );
        var colorInt = tryParseBiomeColor(colorString);
        if (colorInt != null) {
            result.baseSet = true;
            result.base = colorInt;
        }

        colorString = configuration.getString(
            "Grass Color",
            category,
            "",
            String.format(
                "Override the biome's grass color.  Original value: 0x%06x",
                biome.getBiomeGrassColor(0, 0, 0)));
        colorInt = tryParseBiomeColor(colorString);
        if (colorInt != null) {
            result.grassSet = true;
            result.grass = colorInt;
        }

        colorString = configuration.getString(
            "Foliage Color",
            category,
            "",
            String.format(
                "Override the biome's foliage color.  Original value: 0x%06x",
                biome.getBiomeFoliageColor(0, 0, 0)));
        colorInt = tryParseBiomeColor(colorString);
        if (colorInt != null) {
            result.foliageSet = true;
            result.foliage = colorInt;
        }

        colorString = configuration.getString(
            "Sky Color",
            category,
            "",
            String.format(
                "Override the biome's sky color.  Original value: 0x%06x",
                biome.getSkyColorByTemp(biome.temperature)));
        colorInt = tryParseBiomeColor(colorString);
        if (colorInt != null) {
            result.skySet = true;
            result.sky = colorInt;
        }

        colorString = configuration.getString(
            "Water Color",
            category,
            "",
            String.format(
                "Override the biome's water color.  Original value: 0x%06x",
                biome.getWaterColorMultiplier()));
        colorInt = tryParseBiomeColor(colorString);
        if (colorInt != null) {
            result.waterSet = true;
            result.water = colorInt;
        }

        return result;
    }

    private static Integer tryParseBiomeColor(String hexColor) {
        try {
            var color = hexColor.toLowerCase();
            if (color.startsWith("0x")) {
                color = color.substring(2);
            }
            return Integer.parseInt(color, 16);
        } catch (NumberFormatException ex) {
            return null;
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
