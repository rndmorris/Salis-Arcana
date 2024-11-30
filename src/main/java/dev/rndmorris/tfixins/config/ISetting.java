package dev.rndmorris.tfixins.config;

import net.minecraftforge.common.config.Configuration;

public interface ISetting {

    /**
     * Whether the individual setting is enabled.
     */
    boolean isEnabled();

    /**
     * Enable or disable the individual setting. Enabling will have no effect if the parent module is disabled.
     */
    void setEnabled(boolean enabled);

    /**
     * Load this setting from a config file.
     */
    void loadFromConfiguration(Configuration configuration);
}
