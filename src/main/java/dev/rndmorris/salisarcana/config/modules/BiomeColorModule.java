package dev.rndmorris.salisarcana.config.modules;

import javax.annotation.Nonnull;

import dev.rndmorris.salisarcana.config.settings.BiomeColorsSettings;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class BiomeColorModule extends BaseConfigModule {

    public final ToggleSetting acknowledgeDeprecation;

    public final BiomeColorsSettings eerie;
    public final BiomeColorsSettings eldritch;
    public final BiomeColorsSettings magicalForest;
    public final BiomeColorsSettings taint;

    public BiomeColorModule() {
        setEnabled(false); // the module is slated for removal, and should default to disabled

        addSettings(
            acknowledgeDeprecation = new ToggleSetting(
                this,
                "acknowledgeDeprecation",
                "Set to true to acknowledge that the biome color module is being deprecated, and silence the warning message.")
                    .setEnabled(false)
                    .setCategory("_notices"),
            eerie = new BiomeColorsSettings(this, "Eerie", "0x404840", "0x405340", "0x404840", "0x222299", "0x2e535f"),
            eldritch = new BiomeColorsSettings(
                this,
                "Eldritch",
                "0x000000",
                "0x000000",
                "0x000000",
                "0xff7ba5ff",
                "0xffffff"),
            magicalForest = new BiomeColorsSettings(
                this,
                "Magical Forest",
                "0x66f4ab",
                "0x66ffc5",
                "0x55ff81",
                "0xff7aa6ff",
                "0x0077ee"),
            taint = new BiomeColorsSettings(
                this,
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
        return "Notice: This is broken for some users, and slated for removal in a future update.\nOverride the colors of TC4's biomes.";
    }
}
