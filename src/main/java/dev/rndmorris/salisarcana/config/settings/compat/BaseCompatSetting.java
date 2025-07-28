package dev.rndmorris.salisarcana.config.settings.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.IHaveSettings;
import dev.rndmorris.salisarcana.config.settings.Setting;
import dev.rndmorris.salisarcana.mixins.TargetedMod;

public class BaseCompatSetting extends Setting implements IHaveSettings {

    public final TargetedMod mod;

    protected final List<Setting> settings = new ArrayList<>();

    public BaseCompatSetting(IEnabler dependency, TargetedMod mod) {
        super(dependency);
        this.mod = mod;
        setCategory(mod.modId);
    }

    @Override
    public void registerSetting(Setting setting) {
        setting.setCategory(mod.modId);
        IHaveSettings.super.registerSetting(setting);
    }

    @Override
    public boolean isEnabled() {
        return mod.isLoaded() && super.isEnabled();
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration
            .getBoolean(mod.modId, defaultCategory, enabled, String.format("Enable compatibility with %s.", mod.modId));
        for (var setting : settings) {
            setting.setCategory(mod.modId)
                .loadFromConfiguration(configuration);
        }
    }

    @Override
    public List<Setting> getSettings() {
        return settings;
    }
}
