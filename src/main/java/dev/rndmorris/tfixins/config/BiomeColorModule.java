package dev.rndmorris.tfixins.config;

import static dev.rndmorris.tfixins.lib.IntegerHelper.tryParseHexInteger;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

public class BiomeColorModule implements IConfigModule {

    private boolean enabled = false;

    public final BiomeColors eerieBiomeColors;
    public final BiomeColors eldritchBiomeColors;
    public final BiomeColors magicalForestBiomeColors;
    public final BiomeColors taintBiomeColors;

    public BiomeColorModule() {
        final Supplier<IConfigModule> getter = () -> this;
        eerieBiomeColors = new BiomeColors(getter, "eerie", biomeEerie);
        eldritchBiomeColors = new BiomeColors(getter, "eldritch", biomeEldritchLands);
        magicalForestBiomeColors = new BiomeColors(getter, "magical forest", biomeMagicalForest);
        taintBiomeColors = new BiomeColors(getter, "taint", biomeTaint);
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
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        configuration.setCategoryComment(getModuleId(), getModuleComment());

        eerieBiomeColors.loadFromConfiguration(configuration);
        eldritchBiomeColors.loadFromConfiguration(configuration);
        magicalForestBiomeColors.loadFromConfiguration(configuration);
        magicalForestBiomeColors.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class BiomeColors extends Setting {

        private final String biomeName;
        private final BiomeGenBase biome;

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

        public BiomeColors(Supplier<IConfigModule> parentModule, String biomeName, BiomeGenBase biome) {
            super(parentModule);
            this.biomeName = biomeName;
            this.biome = biome;
        }

        @Override
        public void loadFromConfiguration(Configuration configuration) {
            enabled = configuration.getBoolean(
                String.format("Override %s biome colors?", biomeName),
                parentModule.get()
                    .getModuleId(),
                false,
                String.format("Override the colors of the %s biome", biomeName));

            if (!isEnabled() || biome == null) {
                return;
            }

            final var category = String.format(
                "%s_%s",
                parentModule.get()
                    .getModuleId(),
                biomeName.replace(" ", "_"));

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
                baseSet = true;
                base = colorInt;
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
                grassSet = true;
                grass = colorInt;
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
                foliageSet = true;
                foliage = colorInt;
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
                skySet = true;
                sky = colorInt;
            }

            colorString = configuration.getString(
                "Water Color",
                category,
                "",
                String.format(
                    "Override the biome's water color.  Original value: 0x%06x",
                    biome.getWaterColorMultiplier()));
            colorInt = tryParseHexInteger(colorString);
            if (colorInt != null) {
                waterSet = true;
                water = colorInt;
            }
        }
    }

}
