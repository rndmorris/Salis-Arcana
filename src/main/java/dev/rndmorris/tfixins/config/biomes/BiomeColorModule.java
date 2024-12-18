package dev.rndmorris.tfixins.config.biomes;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

public class BiomeColorModule implements IConfigModule {

    private boolean enabled = false;

    public final BiomeColorsSettings eerie;
    public final BiomeColorsSettings eldritch;
    public final BiomeColorsSettings magicalForest;
    public final BiomeColorsSettings taint;

    private final Setting[] settings;

    public BiomeColorModule() {
        settings = new Setting[] {
            eerie = new BiomeColorsSettings(
                this,
                ConfigPhase.EARLY,
                "Eerie",
                "0x404840",
                "0x405340",
                "0x404840",
                "0x222299",
                "0x2e535f"),
            eldritch = new BiomeColorsSettings(
                this,
                ConfigPhase.EARLY,
                "Eldritch",
                "0x000000",
                "0x000000",
                "0x000000",
                "0xff7ba5ff",
                "0xffffff"),
            magicalForest = new BiomeColorsSettings(
                this,
                ConfigPhase.EARLY,
                "Magical Forest",
                "0x66f4ab",
                "0x66ffc5",
                "0x55ff81",
                "0xff7aa6ff",
                "0x0077ee"),
            taint = new BiomeColorsSettings(
                this,
                ConfigPhase.EARLY,
                "Tainted Land",
                "0x6d4189",
                "0x7c6d87",
                "0x6d4189",
                "0x7c44ff",
                "0xcc1188"), };
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
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        for (Setting setting : settings) {
            if (setting.phase == phase) {
                setting.loadFromConfiguration(configuration);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
