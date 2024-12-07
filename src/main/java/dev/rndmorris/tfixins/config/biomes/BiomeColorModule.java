package dev.rndmorris.tfixins.config.biomes;

import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEerie;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeEldritchLands;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeMagicalForest;
import static thaumcraft.common.lib.world.ThaumcraftWorldGenerator.biomeTaint;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;

public class BiomeColorModule implements IConfigModule {

    private boolean enabled = false;

    public final BiomeColorsSettings eerieBiomeColorsSettings;
    public final BiomeColorsSettings eldritchBiomeColorsSettings;
    public final BiomeColorsSettings magicalForestBiomeColorsSettings;
    public final BiomeColorsSettings taintBiomeColorsSettings;

    public BiomeColorModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        eerieBiomeColorsSettings = new BiomeColorsSettings(thisRef, "eerie", biomeEerie);
        eldritchBiomeColorsSettings = new BiomeColorsSettings(thisRef, "eldritch", biomeEldritchLands);
        magicalForestBiomeColorsSettings = new BiomeColorsSettings(thisRef, "magical forest", biomeMagicalForest);
        taintBiomeColorsSettings = new BiomeColorsSettings(thisRef, "taint", biomeTaint);
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

        eerieBiomeColorsSettings.loadFromConfiguration(configuration);
        eldritchBiomeColorsSettings.loadFromConfiguration(configuration);
        magicalForestBiomeColorsSettings.loadFromConfiguration(configuration);
        magicalForestBiomeColorsSettings.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
