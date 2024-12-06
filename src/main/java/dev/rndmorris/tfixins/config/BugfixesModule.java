package dev.rndmorris.tfixins.config;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.bugfixes.BlockCosmeticSolidBeaconFix;

public class BugfixesModule implements IConfigModule {

    private boolean enabled = true;

    public final BlockCosmeticSolidBeaconFix blockCosmeticSolidBeaconFix = new BlockCosmeticSolidBeaconFix(() -> this);
    public final ToggleSetting candleRendererCrashes;
    public final ToggleSetting deadMobsDontAttack;
    public final ToggleSetting itemShardColor;
    public final ToggleSetting useAllBaublesSlots;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting infernalFurnaceDupeFix;

    public BugfixesModule() {
        candleRendererCrashes = new ToggleSetting(
            () -> this,
            "candleMetadataCrash",
            "Fixes several crashes with invalid candle metadata");
        deadMobsDontAttack = new ToggleSetting(
            () -> this,
            "deadMobsDontAttack",
            "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation.");
        itemShardColor = new ToggleSetting(
            () -> this,
            "shardMetadataCrash",
            "Fixes a crash with invalid shard metadata");
        useAllBaublesSlots = new ToggleSetting(
            () -> this,
            "useAllBaublesSlots",
            "Enables support for mods that increase the number of baubles slots.");
        renderRedstoneFix = new ToggleSetting(
            () -> this,
            "renderRedstoneFix",
            "Fixes an issue with some blocks that don't get rendered as normal blocks, not allowing you to push a redstone signal through them.");
        infernalFurnaceDupeFix = new ToggleSetting(
            () -> this,
            "infernalFurnaceDupeFix",
            "Fixes a smelting duplication glitch with the Infernal Furnace");
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "bugfixes";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Fixes for bugs in TC4";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        blockCosmeticSolidBeaconFix.loadFromConfiguration(configuration);
        candleRendererCrashes.loadFromConfiguration(configuration);
        deadMobsDontAttack.loadFromConfiguration(configuration);
        itemShardColor.loadFromConfiguration(configuration);
        useAllBaublesSlots.loadFromConfiguration(configuration);
        renderRedstoneFix.loadFromConfiguration(configuration);
        infernalFurnaceDupeFix.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
