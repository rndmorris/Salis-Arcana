package dev.rndmorris.salisarcana.config.settings;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;

public abstract class Setting implements IEnabler {

    public static final String defaultCategory = "general";
    protected final @Nullable WeakReference<IEnabler> dependencyRef;
    protected boolean enabled = true;
    public ConfigPhase phase;

    private @Nullable String category;

    public Setting(IEnabler dependency, ConfigPhase phase) {
        this.dependencyRef = new WeakReference<>(dependency);
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
     * Whether the individual setting, ands it dependency if it has one, is enabled.
     */
    @Override
    public boolean isEnabled() {
        if (!enabled) {
            return false;
        }
        if (dependencyRef == null) {
            return true;
        }
        final var dependency = dependencyRef.get();
        return dependency != null && dependency.isEnabled();
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
