package dev.rndmorris.salisarcana.config.settings;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class StringArraySetting extends Setting {

    protected final String name;
    protected final String comment;
    protected final String[] defaultValue;
    protected String[] value;

    public StringArraySetting(IEnabler dependency, String name, String comment, String[] defaultValue) {
        super(dependency);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        this.value = configuration.get(getCategory(), this.name, this.defaultValue, this.comment)
            .getStringList();
    }

    public String[] getValue() {
        return value;
    }

    public String[] getNonEmpty() {
        return Arrays.stream(value)
            .filter(
                s -> !s.trim()
                    .isEmpty())
            .toArray(String[]::new);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && !Arrays.equals(value, defaultValue);
    }
}
