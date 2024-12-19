package dev.rndmorris.tfixins.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.modules.IConfigModule;

public class BiomeColorsSettings extends Setting {

    public final HexColorSetting baseColor;
    public final HexColorSetting foliageColor;
    public final HexColorSetting grassColor;
    public final HexColorSetting skyColor;
    public final HexColorSetting waterColorMultiplier;

    private final Setting[] settings;

    public BiomeColorsSettings(IConfigModule parentModule, ConfigPhase phase, String biomeName, String baseColor,
        String foliageColor, String grassColor, String skyColor, String waterColorMultiplier) {
        super(parentModule, phase);

        setCategory(
            biomeName.toLowerCase()
                .replace(' ', '_'));

        settings = new Setting[] {
            this.baseColor = new HexColorSetting(
                parentModule,
                phase,
                "Base Color Override",
                "Override the biome's base color",
                baseColor),
            this.grassColor = new HexColorSetting(
                parentModule,
                phase,
                "Grass Color Override",
                "Override the biome's grass color",
                grassColor),
            this.foliageColor = new HexColorSetting(
                parentModule,
                phase,
                "Foliage Color Override",
                "Override the biome's foliage color",
                foliageColor),
            this.skyColor = new HexColorSetting(
                parentModule,
                phase,
                "Sky Color Override",
                "Override the biome's sky color",
                skyColor),
            this.waterColorMultiplier = new HexColorSetting(
                parentModule,
                phase,
                "Water Color Override",
                "Override the biome's water color",
                waterColorMultiplier), };
        for (var setting : settings) {
            setting.setCategory(getCategory());
        }
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        for (var setting : settings) {
            setting.loadFromConfiguration(configuration);
        }
    }

}
