package dev.rndmorris.tfixins.config.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IEnabler;
import dev.rndmorris.tfixins.config.settings.Setting;

/**
 * A group of related settings that can be collectively enabled or disabled.
 */
public abstract class BaseConfigModule implements IEnabler {

    private boolean enabled = true;
    protected final List<Setting> settings = new ArrayList<>();

    protected void addSettings(Setting... settings) {
        Collections.addAll(this.settings, settings);
    }

    /**
     * Load the module's config settings. Only called if the module is enabled.
     *
     * @param configuration The configuration from which to load the module's settings.
     * @param phase         The current configuration load phase
     */
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        for (var setting : settings) {
            if (setting.phase == phase) {
                setting.loadFromConfiguration(configuration);
            }
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
}
