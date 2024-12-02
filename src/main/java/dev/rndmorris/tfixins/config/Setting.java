package dev.rndmorris.tfixins.config;

import java.util.function.Supplier;

import net.minecraftforge.common.config.Configuration;

public abstract class Setting {

    protected final Supplier<IConfigModule> parentModule;
    protected boolean enabled = true;

    public Setting(Supplier<IConfigModule> getModule) {
        this.parentModule = getModule;
    }

    /**
     * Whether the individual setting is enabled.
     */
    public boolean isEnabled() {
        return enabled && parentModule.get()
            .isEnabled();
    }

    /**
     * Enable or disable the individual setting. Enabling will have no effect if the parent module is disabled.
     */
    public Setting setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Load this setting from a config file.
     */
    public abstract void loadFromConfiguration(Configuration configuration);
}
