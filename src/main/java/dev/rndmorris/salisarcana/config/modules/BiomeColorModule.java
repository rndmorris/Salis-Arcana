package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.settings.BiomeColorsSettings;

public class BiomeColorModule extends BaseConfigModule {

    public final BiomeColorsSettings eerie;
    public final BiomeColorsSettings eldritch;
    public final BiomeColorsSettings magicalForest;
    public final BiomeColorsSettings taint;

    public BiomeColorModule() {
        addSettings(
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
                "0xcc1188"));
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
}
