package dev.rndmorris.tfixins.config.biomes;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.HexColorSetting;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

public class BiomeColorsSettings extends Setting {

    public final HexColorSetting baseColor;
    public final HexColorSetting foliageColor;
    public final HexColorSetting grassColor;
    public final HexColorSetting skyColor;
    public final HexColorSetting waterColorMultiplier;

    private final Setting[] settings;

    public BiomeColorsSettings(IConfigModule parentModule, ConfigPhase phase, String biomeName, String baseColor,
        String foliageColor, String grassColor, String skyColor, String waterColor) {
        super(parentModule, phase);

        final var category = "biome_colors_" + biomeName.toLowerCase()
            .replace(' ', '_');

        settings = new Setting[] {
            this.baseColor = new HexColorSetting(
                parentModule,
                phase,
                category,
                "Base Color Override",
                "Override the biome's base color",
                baseColor),
            this.grassColor = new HexColorSetting(
                parentModule,
                phase,
                category,
                "Grass Color Override",
                "Override the biome's grass color",
                grassColor),
            this.foliageColor = new HexColorSetting(
                parentModule,
                phase,
                category,
                "Foliage Color Override",
                "Override the biome's foliage color",
                foliageColor),
            this.skyColor = new HexColorSetting(
                parentModule,
                phase,
                category,
                "Sky Color Override",
                "Override the biome's sky color",
                skyColor),
            waterColorMultiplier = new HexColorSetting(
                parentModule,
                phase,
                category,
                "Water Color Override",
                "Override the biome's water color",
                waterColor), };
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        for (var setting : settings) {
            setting.loadFromConfiguration(configuration);
        }
    }

}
