package dev.rndmorris.salisarcana.config;

import java.util.Collection;

import dev.rndmorris.salisarcana.config.settings.Setting;

/**
 * Provides methods for getting, adding, and removing settings from an object. This is primarily for groups, but some
 * {@link Setting} derivatives might want to implement it too.
 */
public interface IHaveSettings {

    /**
     * Get this object's collection of setting objects.
     */
    Collection<Setting> getSettings();

    /**
     * Register an individual setting, if it is not already registered.
     *
     * @param setting The setting to register.
     */
    default void registerSetting(Setting setting) {
        final var settings = getSettings();
        if (!settings.contains(setting)) {
            settings.add(setting);
        }
    };

    /**
     * Unregister an individual setting, if it is registered.
     *
     * @param setting The setting to unregister.
     */
    default void unregisterSetting(Setting setting) {
        getSettings().remove(setting);
    }
}
