package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.ConfigPhase;
import dev.rndmorris.salisarcana.config.IEnabler;

public class IntSetting extends Setting {

    private final String name;
    private final String comment;
    private final int defaultValue;
    private int value;
    private int maxValue = Integer.MAX_VALUE;
    private int minValue = Integer.MIN_VALUE;

    public IntSetting(IEnabler dependency, ConfigPhase phase, String name, String comment, int defaultValue) {
        super(dependency, phase);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        value = configuration.getInt(name, getCategory(), value, minValue, maxValue, comment);
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public int getValue() {
        return value;
    }

    public IntSetting setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public IntSetting setMinValue(int minValue) {
        this.minValue = minValue;
        return this;
    }

}
