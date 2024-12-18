package dev.rndmorris.tfixins.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.settings.Setting;

public abstract class BaseConfigModule implements IConfigModule {

    private boolean enabled = true;
    protected final List<Setting> settings = new ArrayList<>();

    protected void addSettings(Setting... settings) {
        Collections.addAll(this.settings, settings);
    }

    @Override
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

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
