package dev.rndmorris.tfixins.config.modules;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IEnabler;

/**
 * A group of related settings that can be collectively enabled or disabled.
 */
public interface IConfigModule extends IEnabler {

    /**
     * The unique id string of the module. Used as part of the module's "Enable" config option name.
     * This should also be the name, or the prefix of the name, of any configuration categories this module reads.
     */
    @Nonnull
    String getModuleId();

    /**
     * The comment string that will be displayed above the module's "Enable" config option.
     */
    @Nonnull
    String getModuleComment();

    /**
     * Whether this module is currently enabled.
     */
    @Override
    boolean isEnabled();

    /**
     * Load the module's config settings. Only called if the module is enabled.
     *
     * @param configuration The configuration from which to load the module's settings.
     * @param phase         The current configuration load phase
     */
    void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase);

    /**
     * Enable or disable this module.
     */
    void setEnabled(boolean enabled);

}
