package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class ToggleSetting extends Setting {

    private final String name;
    private final String comment;

    public ToggleSetting(IEnabler dependency, String name, String comment) {
        super(dependency);
        this.name = name;
        this.comment = comment;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(name, getCategory(), enabled, comment);
    }

    public String getName() {
        return name;
    }

}
