package dev.rndmorris.salisarcana.config.settings;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import javax.annotation.Nullable;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.IDependant;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.IHaveSettings;

public abstract class Setting implements IDependant {

    public static final String defaultCategory = "general";
    protected final @Nullable WeakReference<IEnabler> enabledDependency;
    private @Nullable WeakReference<IHaveSettings> settingOwner;
    protected boolean enabled = true;
    private boolean needsMatchServer = false;

    private @Nullable String category;
    protected final String name;
    protected final String comment;

    private ConfigGroup configGroup = null;

    public Setting(IEnabler dependency, String name, String comment) {
        this.enabledDependency = new WeakReference<>(dependency);
        this.name = name;
        this.comment = comment;
        autoRegisterOwner();
    }

    public Setting(IEnabler dependency, String name) {
        this(dependency, name, "");
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

    public String getName() {
        return this.name;
    }

    public ConfigGroup getConfigGroup() {
        return this.configGroup;
    }

    /**
     * Whether the individual setting, ands it dependency if it has one, is enabled.
     */
    @Override
    public boolean isEnabled() {
        if (!enabled) {
            return false;
        }
        if (enabledDependency == null) {
            return true;
        }
        final var dependency = enabledDependency.get();
        return dependency != null && dependency.isEnabled();
    }

    /**
     * Enable or disable the individual setting. Enabling will have no effect if the parent group is disabled.
     */
    public <T extends Setting> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    /**
     * Whether this setting needs to match the server's setting in multiplayer.
     */
    public <T extends Setting> T setMatchServer(boolean needsMatchServer) {
        this.needsMatchServer = needsMatchServer;
        return (T) this;
    }

    public boolean needsMatchServer() {
        return this.needsMatchServer;
    }

    public IEnabler getDependency() {
        return enabledDependency != null ? enabledDependency.get() : null;
    }

    /**
     * Climb the dependency tree, looking for an {@link IHaveSettings} instance with which to register.
     */
    private void autoRegisterOwner() {
        IEnabler dependency = getDependency();
        var visited = new HashSet<>();
        while (dependency != null) {
            if (visited.contains(dependency)) {
                throw new RuntimeException("Encountered a circular setting dependency!");
            }
            visited.add(dependency);
            if (dependency instanceof IHaveSettings hasSettings) {
                if (dependency instanceof ConfigGroup group) {
                    this.configGroup = group;
                }
                registerTo(hasSettings);
                break;
            }
            if (dependency instanceof IDependant dependant) {
                dependency = dependant.getDependency();
                continue;
            }
            throw new RuntimeException("No IHaveSettings found in the dependency tree.");
        }
    }

    private void unregisterFromOwner() {
        IHaveSettings owner;
        if (settingOwner != null && (owner = settingOwner.get()) != null) {
            owner.unregisterSetting(this);
            settingOwner = null;
        }
    }

    public <T extends Setting> T registerTo(IHaveSettings newOwner) {
        unregisterFromOwner();
        settingOwner = new WeakReference<>(newOwner);
        newOwner.registerSetting(this);
        return (T) this;
    }

    /**
     * Load this setting from a config file.
     */
    public abstract void loadFromConfiguration(Configuration configuration);

    /**
     * String representation of this setting.
     *
     * @return String in the format "groupname:name"
     */
    public String toString() {
        return this.configGroup.getGroupName() + ":" + this.getCategory() + ":" + this.name;
    }
}
