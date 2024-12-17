package dev.rndmorris.tfixins.config.biomes;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

public class BiomeColorModule implements IConfigModule {

    private boolean enabled = false;

    public final BiomeColorsSettings eerieBiomeColorsSettings;
    public final BiomeColorsSettings eldritchBiomeColorsSettings;
    public final BiomeColorsSettings magicalForestBiomeColorsSettings;
    public final BiomeColorsSettings taintBiomeColorsSettings;

    private final Setting[] settings;

    public BiomeColorModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        settings = new Setting[] {

            eerieBiomeColorsSettings = new BiomeColorsSettings(thisRef, ConfigPhase.LATE, "Eerie"),
            eldritchBiomeColorsSettings = new BiomeColorsSettings(thisRef, ConfigPhase.LATE, "Eldritch"),
            magicalForestBiomeColorsSettings = new BiomeColorsSettings(thisRef, ConfigPhase.LATE, "Magical Forest"),
            taintBiomeColorsSettings = new BiomeColorsSettings(thisRef, ConfigPhase.LATE, "Tainted Land"),

        };
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
        configuration.setCategoryComment(getModuleId(), getModuleComment());
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
