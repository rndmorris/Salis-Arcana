package dev.rndmorris.tfixins.config.bugfixes;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.ToggleSetting;

public class BugfixesModule implements IConfigModule {

    private boolean enabled = true;

    public final BlockCosmeticSolidBeaconFix blockCosmeticSolidBeaconFix = new BlockCosmeticSolidBeaconFix(
        new WeakReference<>(this));
    public final ToggleSetting candleRendererCrashes;
    public final ToggleSetting deadMobsDontAttack;
    public final ToggleSetting itemShardColor;
    public final ToggleSetting useAllBaublesSlots;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting infernalFurnaceDupeFix;

    public BugfixesModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        candleRendererCrashes = new ToggleSetting(
            thisRef,
            "candleMetadataCrash",
            "Fixes several crashes with invalid candle metadata");
        deadMobsDontAttack = new ToggleSetting(
            thisRef,
            "deadMobsDontAttack",
            "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation.");
        itemShardColor = new ToggleSetting(thisRef, "shardMetadataCrash", "Fixes a crash with invalid shard metadata");
        useAllBaublesSlots = new ToggleSetting(
            thisRef,
            "useAllBaublesSlots",
            "Enables support for mods that increase the number of baubles slots.");
        renderRedstoneFix = new ToggleSetting(
            thisRef,
            "renderRedstoneFix",
            "Fixes an issue with some blocks that don't get rendered as normal blocks, not allowing you to push a redstone signal through them.");
        infernalFurnaceDupeFix = new ToggleSetting(
            thisRef,
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
