package dev.rndmorris.tfixins.common.biomes;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import dev.rndmorris.tfixins.config.FixinsConfig;

public class BiomeOverrides {

    public static void apply() {
        final var biomeColors = FixinsConfig.biomeColorModule;

        if (biomeColors.eerieBiomeColorsSettings.baseSet && biomeEerie != null) {
            biomeEerie.setColor(biomeColors.eerieBiomeColorsSettings.base);
        }

        if (biomeColors.eldritchBiomeColorsSettings.baseSet && biomeEldritchLands != null) {
            biomeEldritchLands.setColor(biomeColors.eldritchBiomeColorsSettings.base);
        }

        if (biomeColors.magicalForestBiomeColorsSettings.baseSet && biomeMagicalForest != null) {
            biomeMagicalForest.setColor(biomeColors.magicalForestBiomeColorsSettings.base);
        }

        if (biomeColors.taintBiomeColorsSettings.baseSet && biomeTaint != null) {
            biomeTaint.setColor(biomeColors.taintBiomeColorsSettings.base);
        }

    }

}
