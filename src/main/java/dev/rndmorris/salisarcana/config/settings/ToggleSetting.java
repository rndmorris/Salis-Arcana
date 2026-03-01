package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class ToggleSetting extends Setting {

    public ToggleSetting(IEnabler dependency, String name, String comment) {
        super(dependency, name, comment);
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(name, getCategory(), enabled, comment);
    }
}
