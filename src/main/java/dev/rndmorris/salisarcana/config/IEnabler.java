package dev.rndmorris.salisarcana.config;

/**
 * Helper class used in Mixins.java to determine whether you should enable a mixin.
 * Implementations of Setting or ConfigGroup should override this.
 */
public interface IEnabler {

    /**
     * Whether this group or setting is currently enabled.
     */
    boolean isEnabled();
}
