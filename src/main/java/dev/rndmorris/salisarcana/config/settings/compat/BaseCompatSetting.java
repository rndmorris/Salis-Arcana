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
        super(dependency, mod.modId, String.format("Settings for compatibility with %s.", mod.modId));
        this.mod = mod;
        this.setCategory(mod.modId);
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
        enabled = configuration.getBoolean(this.name, defaultCategory, enabled, this.comment);
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
