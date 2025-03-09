package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class StringSetting extends Setting {

    private final String name;
    private final String comment;
    private final String defaultValue;
    private String value;

    public StringSetting(IEnabler dependency, String name, String comment, String defaultValue) {
        super(dependency);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        value = configuration.getString(name, getCategory(), defaultValue, comment);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
