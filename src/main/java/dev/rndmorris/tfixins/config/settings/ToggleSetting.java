package dev.rndmorris.tfixins.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IEnabler;

public class ToggleSetting extends Setting {

    private final String name;
    private final String comment;

    public ToggleSetting(IEnabler dependency, ConfigPhase phase, String name, String comment) {
        super(dependency, phase);
        this.name = name;
        this.comment = comment;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(name, getCategory(), enabled, comment);
    }
}
