package dev.rndmorris.tfixins.config.bugfixes;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;
import dev.rndmorris.tfixins.config.ToggleSetting;

public class BugfixesModule implements IConfigModule {

    private boolean enabled = true;

    public final BlockCosmeticSolidBeaconFix blockCosmeticSolidBeaconFix;
    public final ToggleSetting candleRendererCrashes;
    public final ToggleSetting deadMobsDontAttack;
    public final ToggleSetting infernalFurnaceDupeFix;
    public final ToggleSetting integerInfusionMatrixMath;
    public final ToggleSetting itemShardColor;
    public final ToggleSetting renderRedstoneFix;
    public final ToggleSetting suppressWarpEventsInCreative;
    public final ToggleSetting useAllBaublesSlots;

    private final Setting[] settings;

    public BugfixesModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        settings = new Setting[] {
            blockCosmeticSolidBeaconFix = new BlockCosmeticSolidBeaconFix(thisRef, ConfigPhase.EARLY),
            candleRendererCrashes = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "candleMetadataCrash",
                "Fixes several crashes with invalid candle metadata"),
            deadMobsDontAttack = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "deadMobsDontAttack",
                "Prevents eldritch crabs, all taintacles, and thaumic slimes from attacking during their death animation."),
            infernalFurnaceDupeFix = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "infernalFurnaceDupeFix",
                "Fixes a smelting duplication glitch with the Infernal Furnace"),
            integerInfusionMatrixMath = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "integerInfusionMatrixMath",
                "Calculate infusion stabilizers with integer math instead of floating-point math. This eliminates a rounding error that sometimes makes an infusion altar slightly less stable than it should be."),
            itemShardColor = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "shardMetadataCrash",
                "Fixes a crash with invalid shard metadata"),
            renderRedstoneFix = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "renderRedstoneFix",
                "Fixes an issue with ores where they don't get rendered as normal blocks, not allowing you to push a redstone signal through them."),
            suppressWarpEventsInCreative = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "suppressWarpEventsInCreative",
                "Prevent random warp events from firing for players in creative mode."),
            useAllBaublesSlots = new ToggleSetting(
                thisRef,
                ConfigPhase.EARLY,
                "useAllBaublesSlots",
                "Enables support for mods that increase the number of baubles slots."), };
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
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        for (Setting setting : settings) {
            if (setting.phase == phase) {
                setting.loadFromConfiguration(configuration);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
