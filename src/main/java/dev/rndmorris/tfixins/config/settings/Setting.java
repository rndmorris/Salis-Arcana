package dev.rndmorris.tfixins.config.settings;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IEnabler;

public abstract class Setting implements IEnabler {

    public static final String defaultCategory = "general";
    protected final WeakReference<IEnabler> parentEnabler;
    protected boolean enabled = true;
    public ConfigPhase phase;

    private @Nullable String category;

    public Setting(IEnabler parentSetting, ConfigPhase phase) {
        this.parentEnabler = new WeakReference<>(parentSetting);
        this.phase = phase;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        if (category == null) {
            return defaultCategory;
        }
        return category;
    }

    /**
     * Whether the individual setting, and its parent setting, is enabled.
     */
    @Override
    public boolean isEnabled() {
        IEnabler parent;
        return enabled && (parent = parentEnabler.get()) != null && parent.isEnabled();
    }

    public boolean getEnabled() {
        return enabled;
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
