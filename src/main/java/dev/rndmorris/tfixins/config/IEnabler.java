package dev.rndmorris.tfixins.config;

/**
 * Helper class used in Mixins.java to determine whether you should enable a mixin.
 * Implementations of Setting or IConfigModule should override this.
 */
public interface IEnabler {

    /**
     * Whether this module or setting is currently enabled.
     */
    boolean isEnabled();
}
