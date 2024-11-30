package dev.rndmorris.tfixins.config;

import java.util.function.Supplier;

import net.minecraftforge.common.config.Configuration;

public class Setting implements ISetting {

    protected final Supplier<IConfigModule> parentModule;
    protected boolean enabled = true;

    public Setting(Supplier<IConfigModule> getModule) {
        this.parentModule = getModule;
    }

    @Override
    public boolean isEnabled() {
        return enabled && parentModule.get()
            .isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {}
}
