package dev.rndmorris.tfixins.config;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.bugfixes.BlockCosmeticSolidBeaconFix;

public class BugfixesModule implements IConfigModule {

    private boolean enabled = true;

    public final BlockCosmeticSolidBeaconFix blockCosmeticSolidBeaconFix = new BlockCosmeticSolidBeaconFix(() -> this);

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
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
