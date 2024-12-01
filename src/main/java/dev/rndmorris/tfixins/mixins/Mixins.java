package dev.rndmorris.tfixins.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.tfixins.config.FixinsConfig;

public enum Mixins {

    BIOME_MODULE(Side.BOTH, FixinsConfig.biomeColorModule, "biomes.MixinBiomeGenEerie", "biomes.MixinBiomeGenEldritch",
        "biomes.MixinBiomeGenTaint", "biomes.MixinBiomeGenMagicalForest"),
    BEACON_BLOCKS(Side.BOTH, FixinsConfig.bugfixesModule.blockCosmeticSolidBeaconFix, "blocks.MixinBlockCosmeticSolid"),
    THAUMBOOK_TWEAKS(Side.CLIENT, FixinsConfig.researchBrowserModule, "gui.MixinGuiResearchBrowser");

    private final List<String> classes;
    private final Side side;
    private final IMixinEnabler config;

    Mixins(Side side, IMixinEnabler config, String... classes) {
        this.side = side;
        this.config = config;
        this.classes = Arrays.asList(classes);
    }

    public static List<String> getMixins() {
        final List<String> mixins = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.isEnabled()) {
                mixins.addAll(mixin.classes);
            }
        }
        return mixins;
    }

    private boolean isEnabled() {
        boolean loadSide = this.side.ordinal() == 2 || this.side.ordinal() == FMLLaunchHandler.side()
            .ordinal();
        return loadSide && this.config.isEnabled();
    }

    private enum Side {
        CLIENT,
        SERVER,
        BOTH
    }
}
