package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class BiomeColorsSettings extends Setting {

    public final HexColorSetting baseColor;
    public final HexColorSetting foliageColor;
    public final HexColorSetting grassColor;
    public final HexColorSetting skyColor;
    public final HexColorSetting waterColorMultiplier;

    private final Setting[] settings;

    public BiomeColorsSettings(IEnabler dependency, String biomeName, String baseColor, String foliageColor,
        String grassColor, String skyColor, String waterColorMultiplier) {
        super(dependency);

        setCategory(
            biomeName.toLowerCase()
                .replace(' ', '_'));

        settings = new Setting[] {
            this.baseColor = new HexColorSetting(
                dependency,
                "Base Color Override",
                "Override the biome's base color",
                baseColor),
            this.grassColor = new HexColorSetting(
                dependency,
                "Grass Color Override",
                "Override the biome's grass color",
                grassColor),
            this.foliageColor = new HexColorSetting(
                dependency,
                "Foliage Color Override",
                "Override the biome's foliage color",
                foliageColor),
            this.skyColor = new HexColorSetting(
                dependency,
                "Sky Color Override",
                "Override the biome's sky color",
                skyColor),
            this.waterColorMultiplier = new HexColorSetting(
                dependency,
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
