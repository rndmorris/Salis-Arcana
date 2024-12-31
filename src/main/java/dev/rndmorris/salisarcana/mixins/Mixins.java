// Code here adapted from
// https://github.com/GTNewHorizons/Hodgepodge/blob/master/src/main/java/com/mitchej123/hodgepodge/mixins/Mixins.java
// and therefore under the LGPL-3.0 license.

package dev.rndmorris.salisarcana.mixins;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.Setting;

public enum Mixins {

    // Morpheus
    BIOME_COLOR_EERIE_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eerie.baseColor)
        .addMixinClasses("world.biomes.eerie.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eerie.foliageColor)
        .addMixinClasses("world.biomes.eerie.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eerie.grassColor)
        .addMixinClasses("world.biomes.eerie.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eerie.skyColor)
        .addMixinClasses("world.biomes.eerie.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_EERIE_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eerie.waterColorMultiplier)
        .addMixinClasses("world.biomes.eerie.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_ELDRITCH_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eldritch.baseColor)
        .addMixinClasses("world.biomes.eldritch.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eldritch.foliageColor)
        .addMixinClasses("world.biomes.eldritch.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eldritch.grassColor)
        .addMixinClasses("world.biomes.eldritch.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eldritch.skyColor)
        .addMixinClasses("world.biomes.eldritch.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_ELDRITCH_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.eldritch.waterColorMultiplier)
        .addMixinClasses("world.biomes.eldritch.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_MAGICAL_FOREST_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.magicalForest.baseColor)
        .addMixinClasses("world.biomes.magicalforest.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.magicalForest.foliageColor)
        .addMixinClasses("world.biomes.magicalforest.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.magicalForest.grassColor)
        .addMixinClasses("world.biomes.magicalforest.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.magicalForest.skyColor)
        .addMixinClasses("world.biomes.magicalforest.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_MAGICAL_FOREST_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.magicalForest.waterColorMultiplier)
        .addMixinClasses("world.biomes.magicalforest.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    BIOME_COLOR_TAINT_BASE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.taint.baseColor)
        .addMixinClasses("world.biomes.taint.MixinBaseColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_FOLIAGE_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.taint.foliageColor)
        .addMixinClasses("world.biomes.taint.MixinFoliageColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_GRASS_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.taint.grassColor)
        .addMixinClasses("world.biomes.taint.MixinGrassColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_SKY_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.taint.skyColor)
        .addMixinClasses("world.biomes.taint.MixinSkyColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BIOME_COLOR_TAINT_WATER_COLOR(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.biomeColors.taint.waterColorMultiplier)
        .addMixinClasses("world.biomes.taint.MixinWaterColor")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    // Bugfixes
    ARCANE_FURNACE_DUPE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.infernalFurnaceDupeFix)
        .addMixinClasses("blocks.MixinBlockArcaneFurnace")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BEACON_BLOCKS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.beaconBlockFixSetting)
        .addMixinClasses("blocks.MixinBlockCosmeticSolid")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    BLOCKCANDLE_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.candleRendererCrashes)
        .addMixinClasses("blocks.MixinBlockCandleRenderer", "blocks.MixinBlockCandle")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    DEAD_MOBS_DONT_ATTACK(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.deadMobsDontAttack)
        .addMixinClasses(
            "entities.MixinEntityTaintacle",
            "entities.MixinEntityEldritchCrab",
            "entities.MixinEntityThaumicSlime")),
    INTEGER_INFUSION_MATRIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .applyIf(
            () -> ConfigModuleRoot.bugfixes.integerInfusionMatrixMath.isEnabled()
                && !ConfigModuleRoot.enhancements.stabilizerRewrite.isEnabled())
        .addMixinClasses("tiles.MixinTileInfusionMatrix_IntegerStabilizers")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    ITEMSHARD_OOB(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.itemShardColor)
        .addMixinClasses("items.MixinItemShard")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RENDER_REDSTONE_FIX(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.renderRedstoneFix)
        .addMixinClasses("blocks.MixinBlockCustomOre")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    STRICT_INFUSION_INPUTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.bugfixes.strictInfusionMatrixInputChecks)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_InputEnforcement")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    // Enhancements
    EXTENDED_BAUBLES_SUPPORT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.useAllBaublesSlots)
        .addMixinClasses("events.MixinEventHandlerRunic", "items.MixinWandManager")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    EXTENDED_BAUBLES_SUPPORT_CLIENT(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setConfig(ConfigModuleRoot.enhancements.useAllBaublesSlots)
        .addMixinClasses("gui.MixinREHWandHandler")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    SUPPRESS_CREATIVE_WARP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.suppressWarpEventsInCreative)
        .addMixinClasses("events.MixinEventHandlerEntity")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    CTRL_SCROLL_NAVIGATION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setConfig(ConfigModuleRoot.enhancements.nomiconScrollwheelEnabled)
        .addMixinClasses("gui.MixinGuiResearchBrowser")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RESEARCH_ID_POPUP(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setConfig(ConfigModuleRoot.enhancements.nomiconShowResearchId)
        .addMixinClasses("gui.MixinGuiResearchBrowser")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    RIGHT_CLICK_NAVIAGTION(new Builder().setPhase(Phase.LATE)
        .setSide(Side.CLIENT)
        .setConfig(ConfigModuleRoot.enhancements.nomiconRightClickClose)
        .addMixinClasses("gui.MixinGuiResearchBrowser", "gui.MixinGuiResearchRecipe")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    NODE_GENERATION_MODIFIER_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.nodeModifierWeights)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),
    NODE_GENERATION_TYPE_WEIGHTS(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.nodeTypeWeights)
        .addMixinClasses("world.MixinThaumcraftWorldGenerator")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    STABILIZER_REWRITE(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.stabilizerRewrite)
        .addMixinClasses("tiles.MixinTileInfusionMatrix_StabilizerRewrite")
        .addTargetedMod(TargetedMod.THAUMCRAFT)),

    WAND_PEDESTAL_CV(new Builder().setPhase(Phase.LATE)
        .setSide(Side.BOTH)
        .setConfig(ConfigModuleRoot.enhancements.wandPedestalUseCV)
        .addMixinClasses("tiles.MixinTileWandPedestal")
        .addTargetedMod(TargetedMod.THAUMCRAFT)
        .addExcludedMod(TargetedMod.HODGEPODGE))

    ;

    private final List<String> mixinClasses;
    private final List<TargetedMod> targetedMods;
    private final List<TargetedMod> excludedMods;
    private final IEnabler config;
    private final Supplier<Boolean> applyIf;
    private final Phase phase;
    private final Side side;

    Mixins(Builder builder) {
        this.mixinClasses = builder.mixinClasses;
        this.targetedMods = builder.targetedMods;
        this.excludedMods = builder.excludedMods;
        this.config = builder.config;
        this.applyIf = this.config::isEnabled;
        this.phase = builder.phase;
        this.side = builder.side;
        if (this.mixinClasses.isEmpty()) {
            throw new RuntimeException("No mixin class specified for Mixin : " + this.name());
        }
        if (this.targetedMods.isEmpty()) {
            throw new RuntimeException("No targeted mods specified for Mixin : " + this.name());
        }
        if (this.phase == null) {
            throw new RuntimeException("No Phase specified for Mixin : " + this.name());
        }
        if (this.side == null) {
            throw new RuntimeException("No new Builder().setSide(Side function specified for Mixin : " + this.name());
        }
    }

    public static List<String> getEarlyMixins(Set<String> loadedCoreMods) {
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.phase == Phase.EARLY) {
                if (mixin.shouldLoad(loadedCoreMods, Collections.emptySet())) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOG.info("Not loading the following EARLY mixins: {}", notLoading.toString());
        return mixins;
    }

    public static List<String> getLateMixins(Set<String> loadedMods) {
        // NOTE: Any targetmod here needs a modid, not a coremod id
        final List<String> mixins = new ArrayList<>();
        final List<String> notLoading = new ArrayList<>();
        for (Mixins mixin : Mixins.values()) {
            if (mixin.phase == Phase.LATE) {
                if (mixin.shouldLoad(Collections.emptySet(), loadedMods)) {
                    mixins.addAll(mixin.mixinClasses);
                } else {
                    notLoading.addAll(mixin.mixinClasses);
                }
            }
        }
        LOG.info("Not loading the following LATE mixins: {}", notLoading.toString());
        return mixins;
    }

    private boolean shouldLoadSide() {
        return side == Side.BOTH || (side == Side.SERVER && FMLLaunchHandler.side()
            .isServer())
            || (side == Side.CLIENT && FMLLaunchHandler.side()
                .isClient());
    }

    private boolean allModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return false;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && !loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && !loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean noModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods, Set<String> loadedMods) {
        if (targetedMods.isEmpty()) return true;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            // Check coremod first
            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && loadedCoreMods.contains(target.coreModClass)) return false;
            else if (!loadedMods.isEmpty() && target.modId != null && loadedMods.contains(target.modId)) return false;
        }

        return true;
    }

    private boolean shouldLoad(Set<String> loadedCoreMods, Set<String> loadedMods) {
        return (shouldLoadSide() && applyIf.get()
            && allModsLoaded(targetedMods, loadedCoreMods, loadedMods)
            && noModsLoaded(excludedMods, loadedCoreMods, loadedMods));
    }

    private static class Builder {

        private final String name;
        private final List<String> mixinClasses = new ArrayList<>();
        private final List<TargetedMod> targetedMods = new ArrayList<>();
        private final List<TargetedMod> excludedMods = new ArrayList<>();
        private Supplier<Boolean> applyIf = null;
        private IEnabler config = null;
        private Phase phase = null;
        private Side side = null;

        public Builder() {
            this("");
        }

        public Builder(String name) {
            this.name = name;
        }

        public Builder addMixinClasses(String... mixinClasses) {
            this.mixinClasses.addAll(Arrays.asList(mixinClasses));
            return this;
        }

        public Builder setPhase(Phase phase) {
            if (this.phase != null) {
                throw new RuntimeException("Trying to define Phase twice for " + this.name);
            }
            this.phase = phase;
            return this;
        }

        public Builder setSide(Side side) {
            if (this.side != null) {
                throw new RuntimeException("Trying to define new Builder().setSide(Side twice for " + this.name);
            }
            this.side = side;
            return this;
        }

        public Builder setConfig(Setting config) {
            this.config = config;
            return this;
        }

        public Builder addTargetedMod(TargetedMod mod) {
            this.targetedMods.add(mod);
            return this;
        }

        public Builder addExcludedMod(TargetedMod mod) {
            this.excludedMods.add(mod);
            return this;
        }

        public Builder applyIf(Supplier<Boolean> applyIf) {
            this.applyIf = applyIf;
            return this;
        }
    }

    private enum Side {
        BOTH,
        CLIENT,
        SERVER
    }

    private enum Phase {
        EARLY,
        LATE,
    }
}
