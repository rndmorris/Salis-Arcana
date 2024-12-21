package dev.rndmorris.salisarcana.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.IEnabler;

public enum Mixins {

    // spotless:off

    // Biome colors
    BIOME_COLOR_EERIE_BASE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eerie.baseColor, "world.biomes.eerie.MixinBaseColor"),
    BIOME_COLOR_EERIE_FOLIAGE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eerie.foliageColor, "world.biomes.eerie.MixinFoliageColor"),
    BIOME_COLOR_EERIE_GRASS_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eerie.grassColor, "world.biomes.eerie.MixinGrassColor"),
    BIOME_COLOR_EERIE_SKY_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eerie.skyColor, "world.biomes.eerie.MixinSkyColor"),
    BIOME_COLOR_EERIE_WATER_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eerie.waterColorMultiplier, "world.biomes.eerie.MixinWaterColor"),

    BIOME_COLOR_ELDRITCH_BASE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eldritch.baseColor, "world.biomes.eldritch.MixinBaseColor"),
    BIOME_COLOR_ELDRITCH_FOLIAGE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eldritch.foliageColor, "world.biomes.eldritch.MixinFoliageColor"),
    BIOME_COLOR_ELDRITCH_GRASS_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eldritch.grassColor, "world.biomes.eldritch.MixinGrassColor"),
    BIOME_COLOR_ELDRITCH_SKY_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eldritch.skyColor, "world.biomes.eldritch.MixinSkyColor"),
    BIOME_COLOR_ELDRITCH_WATER_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.eldritch.waterColorMultiplier, "world.biomes.eldritch.MixinWaterColor"),

    BIOME_COLOR_MAGICAL_FOREST_BASE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.magicalForest.baseColor, "world.biomes.magicalforest.MixinBaseColor"),
    BIOME_COLOR_MAGICAL_FOREST_FOLIAGE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.magicalForest.foliageColor, "world.biomes.magicalforest.MixinFoliageColor"),
    BIOME_COLOR_MAGICAL_FOREST_GRASS_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.magicalForest.grassColor, "world.biomes.magicalforest.MixinGrassColor"),
    BIOME_COLOR_MAGICAL_FOREST_SKY_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.magicalForest.skyColor, "world.biomes.magicalforest.MixinSkyColor"),
    BIOME_COLOR_MAGICAL_FOREST_WATER_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.magicalForest.waterColorMultiplier, "world.biomes.magicalforest.MixinWaterColor"),

    BIOME_COLOR_TAINT_BASE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.taint.baseColor, "world.biomes.taint.MixinBaseColor"),
    BIOME_COLOR_TAINT_FOLIAGE_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.taint.foliageColor, "world.biomes.taint.MixinFoliageColor"),
    BIOME_COLOR_TAINT_GRASS_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.taint.grassColor, "world.biomes.taint.MixinGrassColor"),
    BIOME_COLOR_TAINT_SKY_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.taint.skyColor, "world.biomes.taint.MixinSkyColor"),
    BIOME_COLOR_TAINT_WATER_COLOR(MixinSide.BOTH, ConfigModuleRoot.biomeColors.taint.waterColorMultiplier, "world.biomes.taint.MixinWaterColor"),

    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(MixinSide.BOTH, ConfigModuleRoot.bugfixes.infernalFurnaceDupeFix, "blocks.MixinBlockArcaneFurnace"),
    BEACON_BLOCKS(MixinSide.BOTH, ConfigModuleRoot.bugfixes.beaconBlockFixSetting, "blocks.MixinBlockCosmeticSolid"),
    BLOCKCANDLE_OOB(MixinSide.BOTH, ConfigModuleRoot.bugfixes.candleRendererCrashes, "blocks.MixinBlockCandleRenderer", "blocks.MixinBlockCandle"),
    DEAD_MOBS_DONT_ATTACK(MixinSide.BOTH, ConfigModuleRoot.bugfixes.deadMobsDontAttack, "entities.MixinEntityTaintacle", "entities.MixinEntityEldritchCrab", "entities.MixinEntityThaumicSlime"),
    INTEGER_INFUSION_MATRIX(MixinSide.BOTH, ConfigModuleRoot.bugfixes.integerInfusionMatrixMath, "tiles.MixinTileInfusionMatrix_IntegerStabilizers"),
    ITEMSHARD_OOB(MixinSide.BOTH, ConfigModuleRoot.bugfixes.itemShardColor, "items.MixinItemShard"),
    RENDER_REDSTONE_FIX(MixinSide.BOTH, ConfigModuleRoot.bugfixes.renderRedstoneFix, "blocks.MixinBlockCustomOre"),
    STRICT_INFUSION_INPUTS(MixinSide.BOTH, ConfigModuleRoot.bugfixes.strictInfusionMatrixInputChecks, "tiles.MixinTileInfusionMatrix_InputEnforcement"),

    // Enhancements
    EXTENDED_BAUBLES_SUPPORT(MixinSide.BOTH, ConfigModuleRoot.enhancements.useAllBaublesSlots, "events.MixinEventHandlerRunic", "items.MixinWandManager"),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(MixinSide.CLIENT, ConfigModuleRoot.enhancements.useAllBaublesSlots, "gui.MixinREHWandHandler"),
    SUPPRESS_CREATIVE_WARP(MixinSide.BOTH, ConfigModuleRoot.enhancements.suppressWarpEventsInCreative, "events.MixinEventHandlerEntity"),

    CTRL_SCROLL_NAVIGATION(MixinSide.CLIENT, ConfigModuleRoot.enhancements.nomiconScrollwheelEnabled, "gui.MixinGuiResearchBrowser"),
    RESEARCH_ID_POPUP(MixinSide.CLIENT, ConfigModuleRoot.enhancements.nomiconShowResearchId, "gui.MixinGuiResearchBrowser"),
    RIGHT_CLICK_NAVIAGTION(MixinSide.CLIENT, ConfigModuleRoot.enhancements.nomiconRightClickClose, "gui.MixinGuiResearchBrowser", "gui.MixinGuiResearchRecipe"),

    NODE_GENERATION_MODIFIER_WEIGHTS(MixinSide.BOTH, ConfigModuleRoot.enhancements.nodeModifierWeights, "world.MixinThaumcraftWorldGenerator"),
    NODE_GENERATION_TYPE_WEIGHTS(MixinSide.BOTH, ConfigModuleRoot.enhancements.nodeTypeWeights, "world.MixinThaumcraftWorldGenerator"),

    ;
    // spotless:on

    private final List<String> classes;
    private final MixinSide side;
    private final IEnabler config;

    public static List<String> getMixins(Set<String> loadedMods) {
        final List<String> mixins = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.isEnabled() && mixin.modlistPredicates(loadedMods)) {
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

    private boolean modlistPredicates(Set<String> loadedMods) {
        return switch (this) {
            case BLOCKCANDLE_OOB, ITEMSHARD_OOB -> doesNotHaveDuplicateCandleOrShardFix(loadedMods);
            default -> true;
        };
    }

    private boolean doesNotHaveDuplicateCandleOrShardFix(Set<String> loadedMods) {
        return !(loadedMods.contains("bugtorch") || loadedMods.contains("thaumicmixins"));
    }
}
