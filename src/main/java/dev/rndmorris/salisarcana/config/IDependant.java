package dev.rndmorris.salisarcana.config;

import javax.annotation.Nullable;

/**
 * A configuration element that depends on another to be active.
 */
public interface IDependant extends IEnabler {

    /**
     * Get this element's parent dependency, if it has one. Groups should never. Settings always should.
     *
     * @return The element's dependency.
     */
    @Nullable
    IEnabler getDependency();
}
