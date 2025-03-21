package dev.rndmorris.salisarcana.config.settings.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Loader;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.IHaveSettings;
import dev.rndmorris.salisarcana.config.settings.Setting;

public class BaseCompatSetting extends Setting implements IHaveSettings {

    public final String modId;

    protected final List<Setting> settings = new ArrayList<>();

    public BaseCompatSetting(IEnabler dependency, String modId) {
        super(dependency);
        this.modId = modId;
        setCategory(modId);
    }

    @Override
    public void registerSetting(Setting setting) {
        setting.setCategory(modId);
        IHaveSettings.super.registerSetting(setting);
    }

    @Override
    public boolean isEnabled() {
        return Loader.isModLoaded(modId) && super.isEnabled();
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration
            .getBoolean(modId, defaultCategory, enabled, String.format("Enable compatibility with %s.", modId));
        for (var setting : settings) {
            setting.setCategory(modId)
                .loadFromConfiguration(configuration);
        }
    }

    @Override
    public List<Setting> getSettings() {
        return settings;
    }
}
