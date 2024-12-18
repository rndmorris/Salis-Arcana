package dev.rndmorris.tfixins.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import dev.rndmorris.tfixins.config.FixinsConfig;
import dev.rndmorris.tfixins.config.IEnabler;

public enum Mixins {

    // spotless:off

    // Biome colors
    BIOME_COLOR_EERIE_BASE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eerie.baseColor, "world.biomes.eerie.MixinBaseColor"),
    BIOME_COLOR_EERIE_FOLIAGE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eerie.foliageColor, "world.biomes.eerie.MixinFoliageColor"),
    BIOME_COLOR_EERIE_GRASS_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eerie.grassColor, "world.biomes.eerie.MixinGrassColor"),
    BIOME_COLOR_EERIE_SKY_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eerie.skyColor, "world.biomes.eerie.MixinSkyColor"),
    BIOME_COLOR_EERIE_WATER_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eerie.waterColorMultiplier, "world.biomes.eerie.MixinWaterColor"),

    BIOME_COLOR_ELDRITCH_BASE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eldritch.baseColor, "world.biomes.eldritch.MixinBaseColor"),
    BIOME_COLOR_ELDRITCH_FOLIAGE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eldritch.foliageColor, "world.biomes.eldritch.MixinFoliageColor"),
    BIOME_COLOR_ELDRITCH_GRASS_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eldritch.grassColor, "world.biomes.eldritch.MixinGrassColor"),
    BIOME_COLOR_ELDRITCH_SKY_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eldritch.skyColor, "world.biomes.eldritch.MixinSkyColor"),
    BIOME_COLOR_ELDRITCH_WATER_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.eldritch.waterColorMultiplier, "world.biomes.eldritch.MixinWaterColor"),

    BIOME_COLOR_MAGICAL_FOREST_BASE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.magicalForest.baseColor, "world.biomes.magicalforest.MixinBaseColor"),
    BIOME_COLOR_MAGICAL_FOREST_FOLIAGE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.magicalForest.foliageColor, "world.biomes.magicalforest.MixinFoliageColor"),
    BIOME_COLOR_MAGICAL_FOREST_GRASS_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.magicalForest.grassColor, "world.biomes.magicalforest.MixinGrassColor"),
    BIOME_COLOR_MAGICAL_FOREST_SKY_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.magicalForest.skyColor, "world.biomes.magicalforest.MixinSkyColor"),
    BIOME_COLOR_MAGICAL_FOREST_WATER_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.magicalForest.waterColorMultiplier, "world.biomes.magicalforest.MixinWaterColor"),

    BIOME_COLOR_TAINT_BASE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.taint.baseColor, "world.biomes.taint.MixinBaseColor"),
    BIOME_COLOR_TAINT_FOLIAGE_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.taint.foliageColor, "world.biomes.taint.MixinFoliageColor"),
    BIOME_COLOR_TAINT_GRASS_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.taint.grassColor, "world.biomes.taint.MixinGrassColor"),
    BIOME_COLOR_TAINT_SKY_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.taint.skyColor, "world.biomes.taint.MixinSkyColor"),
    BIOME_COLOR_TAINT_WATER_COLOR(MixinSide.BOTH, FixinsConfig.biomeColorModule.taint.waterColorMultiplier, "world.biomes.taint.MixinWaterColor"),

    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(MixinSide.BOTH, FixinsConfig.bugfixesModule.infernalFurnaceDupeFix, "blocks.MixinBlockArcaneFurnace"),
    BEACON_BLOCKS(MixinSide.BOTH, FixinsConfig.bugfixesModule.blockCosmeticSolidBeaconFix, "blocks.MixinBlockCosmeticSolid"),
    BLOCKCANDLE_OOB(MixinSide.BOTH, FixinsConfig.bugfixesModule.candleRendererCrashes, "blocks.MixinBlockCandleRenderer", "blocks.MixinBlockCandle"),
    DEAD_MOBS_DONT_ATTACK(MixinSide.BOTH, FixinsConfig.bugfixesModule.deadMobsDontAttack, "entities.MixinEntityTaintacle", "entities.MixinEntityEldritchCrab", "entities.MixinEntityThaumicSlime"),
    INTEGER_INFUSION_MATRIX(MixinSide.BOTH, FixinsConfig.bugfixesModule.integerInfusionMatrixMath, "tiles.MixinTileInfusionMatrix_IntegerStabilizers"),
    ITEMSHARD_OOB(MixinSide.BOTH, FixinsConfig.bugfixesModule.itemShardColor, "items.MixinItemShard"),
    RENDER_REDSTONE_FIX(MixinSide.BOTH, FixinsConfig.bugfixesModule.renderRedstoneFix, "blocks.MixinBlockCustomOre"),
    STRICT_INFUSION_INPUTS(MixinSide.BOTH, FixinsConfig.bugfixesModule.strictInfusionMatrixInputChecks, "tiles.MixinTileInfusionMatrix_InputEnforcement"),

    // Enhancements
    EXTENDED_BAUBLES_SUPPORT(MixinSide.BOTH, FixinsConfig.enhancements.useAllBaublesSlots, "events.MixinEventHandlerRunic", "items.MixinWandManager"),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(MixinSide.CLIENT, FixinsConfig.enhancements.useAllBaublesSlots, "gui.MixinREHWandHandler"),
    SUPPRESS_CREATIVE_WARP(MixinSide.BOTH, FixinsConfig.enhancements.suppressWarpEventsInCreative, "events.MixinEventHandlerEntity"),

    CTRL_SCROLL_NAVIGATION(MixinSide.CLIENT, FixinsConfig.enhancements.scrollwheelEnabled, "gui.MixinGuiResearchBrowser"),
    RESEARCH_ID_POPUP(MixinSide.CLIENT, FixinsConfig.enhancements.showResearchId, "gui.MixinGuiResearchBrowser"),
    RIGHT_CLICK_NAVIAGTION(MixinSide.CLIENT, FixinsConfig.enhancements.rightClickClose, "gui.MixinGuiResearchBrowser", "gui.MixinGuiResearchRecipe"),

    NODE_GENERATION_MODIFIER_WEIGHTS(MixinSide.BOTH, FixinsConfig.enhancements.nodeModifierWeights, "world.MixinThaumcraftWorldGenerator"),
    NODE_GENERATION_TYPE_WEIGHTS(MixinSide.BOTH, FixinsConfig.enhancements.nodeTypeWeights, "world.MixinThaumcraftWorldGenerator"),

    ;
    // spotless:on

    private final List<String> classes;
    private final MixinSide side;
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

    Mixins(MixinSide side, IEnabler config, String... classes) {
        this.side = side;
        this.config = config;
        this.classes = Arrays.asList(classes);
    }

    private boolean isEnabled() {
        return side.matchesFMLSide(FMLLaunchHandler.side()) && this.config.isEnabled();
    }

    private enum MixinSide {

        CLIENT,
        SERVER,
        BOTH;

        public boolean matchesFMLSide(Side side) {
            return switch (this) {
                case BOTH -> true;
                case CLIENT -> side == Side.CLIENT;
                case SERVER -> side == Side.SERVER;
            };
        }
    }
}
