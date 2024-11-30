package dev.rndmorris.tfixins.config;

import java.util.function.Supplier;

import net.minecraftforge.common.config.Configuration;

public abstract class Setting {

    protected final Supplier<IConfigModule> parentModule;
    protected boolean enabled = true;

    public Setting(Supplier<IConfigModule> getModule) {
        this.parentModule = getModule;
    }

    public boolean isEnabled() {
        return enabled && parentModule.get()
            .isEnabled();
    }

    public Setting setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public abstract void loadFromConfiguration(Configuration configuration);
}
