package dev.rndmorris.tfixins.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.tfixins.config.FixinsConfig;
import dev.rndmorris.tfixins.config.IEnabler;

public enum Mixins {

    BIOME_EERIE_MODULE(Side.BOTH, FixinsConfig.biomeColorModule, "biomes.MixinBiomeGenEerie"),
    BIOME_ELDRITCH_MODULE(Side.BOTH, FixinsConfig.biomeColorModule, "biomes.MixinBiomeGenEldrith"),
    BIOME_TAINT_MODULE(Side.BOTH, FixinsConfig.biomeColorModule, "biomes.MixinBiomeGenTaint"),
    BIOME_MAGICALFOREST_MODULE(Side.BOTH, FixinsConfig.biomeColorModule, "biomes.MixinBiomeGenMagicalForest"),
    BEACON_BLOCKS(Side.BOTH, FixinsConfig.bugfixesModule.blockCosmeticSolidBeaconFix, "blocks.MixinBlockCosmeticSolid"),
    RIGHT_CLICK_NAVIAGTION(Side.CLIENT, FixinsConfig.researchBrowserModule.rightClickClose,
        "gui.MixinGuiResearchBrowser", "gui.MixinGuiResearchRecipe"),
    CTRL_SCROLL_NAVIGATION(Side.CLIENT, FixinsConfig.researchBrowserModule.scrollwheelEnabled,
        "gui.MixinGuiResearchBrowser"),
    RESEARCH_ID_POPUP(Side.CLIENT, FixinsConfig.researchBrowserModule.showResearchId, "gui.MixinGuiResearchBrowser"),
    ITEMSHARD_OOB(Side.BOTH, FixinsConfig.bugfixesModule.itemShardColor, "items.MixinItemShard"),
    BLOCKCANDLE_OOB(Side.BOTH, FixinsConfig.bugfixesModule.candleRendererCrashes, "blocks.MixinBlockCandleRenderer",
        "blocks.MixinBlockCandle"),
    DEAD_MOBS_DONT_ATTACK(Side.BOTH, FixinsConfig.bugfixesModule.deadMobsDontAttack, "entities.MixinEntityTaintacle",
        "entities.MixinEntityEldritchCrab");

    private final List<String> classes;
    private final Side side;
    private final IEnabler config;

    public static List<String> getMixins() {
        final List<String> mixins = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.isEnabled()) {
                mixins.addAll(mixin.classes);
            }
        }
        return mixins;
    }

    Mixins(Side side, IEnabler config, String... classes) {
        this.side = side;
        this.config = config;
        this.classes = Arrays.asList(classes);
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
