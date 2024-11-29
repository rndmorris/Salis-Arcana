package dev.rndmorris.tfixins.config;

import java.util.function.Supplier;

public class Setting implements ISetting {

    protected final Supplier<Boolean> moduleEnabled;
    protected boolean enabled = true;

    public Setting(Supplier<Boolean> moduleEnabled) {
        this.moduleEnabled = moduleEnabled;
    }

    public boolean isEnabled() {
        return enabled && moduleEnabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
