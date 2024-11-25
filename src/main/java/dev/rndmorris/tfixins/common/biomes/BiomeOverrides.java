package dev.rndmorris.tfixins.common.biomes;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import dev.rndmorris.tfixins.config.FixinsConfig;

public class BiomeOverrides {

    public static void apply() {
        final var biomeColors = FixinsConfig.biomeColorModule;

        if (biomeColors.eerieBiomeColors.baseSet && biomeEerie != null) {
            biomeEerie.setColor(biomeColors.eerieBiomeColors.base);
        }

        if (biomeColors.eldritchBiomeColors.baseSet && biomeEldritchLands != null) {
            biomeEldritchLands.setColor(biomeColors.eldritchBiomeColors.base);
        }

        if (biomeColors.magicalForestBiomeColors.baseSet && biomeMagicalForest != null) {
            biomeMagicalForest.setColor(biomeColors.magicalForestBiomeColors.base);
        }

        if (biomeColors.taintBiomeColors.baseSet && biomeTaint != null) {
            biomeTaint.setColor(biomeColors.taintBiomeColors.base);
        }

    }

}
