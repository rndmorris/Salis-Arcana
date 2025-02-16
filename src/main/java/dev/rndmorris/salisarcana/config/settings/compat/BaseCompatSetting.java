package dev.rndmorris.salisarcana.config.settings.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Loader;
import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.config.settings.Setting;

public class BaseCompatSetting extends Setting {

    public final String modId;

    protected final List<Setting> settings = new ArrayList<>();

    public BaseCompatSetting(IEnabler dependency, ConfigPhase phase, String modId) {
        super(dependency, phase);
        this.modId = modId;
        setCategory(modId);
    }

    protected void addSettings(Setting... settings) {
        Collections.addAll(this.settings, settings);
        for (var setting : settings) {
            if (defaultCategory.equals(setting.getCategory())) {
                setting.setCategory(modId);
            }
        }
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
            setting.loadFromConfiguration(configuration);
        }
    }
}
