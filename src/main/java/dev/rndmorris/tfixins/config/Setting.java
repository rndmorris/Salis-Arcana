package dev.rndmorris.tfixins.config;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

public abstract class Setting implements IEnabler {

    protected final WeakReference<IConfigModule> moduleRef;
    protected boolean enabled = true;

    public Setting(WeakReference<IConfigModule> getModule) {
        this.moduleRef = getModule;
    }

    /**
     * Whether the individual setting is enabled.
     */
    @Override
    public boolean isEnabled() {
        IConfigModule parent;
        return enabled && (parent = moduleRef.get()) != null && parent.isEnabled();
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
