package dev.rndmorris.salisarcana.config.settings;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public abstract class Setting implements IEnabler {

    public static final String defaultCategory = "general";
    protected final @Nullable WeakReference<IEnabler> dependencyRef;
    protected boolean enabled = true;

    private @Nullable String category;

    public Setting(IEnabler dependency) {
        this.dependencyRef = new WeakReference<>(dependency);
    }

    public <T extends Setting> T setCategory(String category) {
        this.category = category;
        return (T) this;
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
    public <T extends Setting> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    /**
     * Load this setting from a config file.
     */
    public abstract void loadFromConfiguration(Configuration configuration);
}
