package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class FloatSetting extends Setting {

    private final String name;
    private final String comment;
    private final float defaultValue;
    private float value;
    private float maxValue = Float.MAX_VALUE;
    private float minValue = -Float.MAX_VALUE; // Float.MIN_VALUE is the smallest positive value, not the most negative

    public FloatSetting(IEnabler dependency, String name, String comment, float defaultValue) {
        super(dependency);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        value = configuration.getFloat(name, getCategory(), defaultValue, minValue, maxValue, comment);
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getValue() {
        return value;
    }

    public FloatSetting setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public FloatSetting setMinValue(float minValue) {
        this.minValue = minValue;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return value != defaultValue && super.isEnabled();
    }

}
