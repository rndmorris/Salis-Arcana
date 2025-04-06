package dev.rndmorris.salisarcana.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.settings.Setting;

/**
 * A group of related settings that can be collectively enabled or disabled.
 */
public abstract class ModuleBase implements IEnabler, IHaveSettings {

    private boolean enabled = true;
    protected final List<Setting> settings = new ArrayList<>();

    public ModuleBase() {
        Config.modules.add(this);
    }

    /**
     * Load the module's config settings. Only called if the module is enabled.
     *
     * @param configuration The configuration from which to load the module's settings.
     */
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        for (var setting : settings) {
            setting.loadFromConfiguration(configuration);

        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable this module.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The unique id string of the module. Used as part of the module's "Enable" config option name.
     * This should also be the name, or the prefix of the name, of any configuration categories this module reads.
     */
    @Nonnull
    public abstract String getModuleId();

    /**
     * The comment string that will be displayed above the module's "Enable" config option.
     */
    @Nonnull
    public abstract String getModuleComment();

    @Override
    public Collection<Setting> getSettings() {
        return settings;
    }
}
