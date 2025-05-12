package dev.rndmorris.salisarcana.config.settings;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;
import dev.rndmorris.salisarcana.core.SalisArcanaCore;

public class IntArraySetting extends Setting {

    protected final String name;
    protected final String comment;
    protected final int[] defaultValue;
    protected int[] value;
    protected int minValue;
    protected int maxValue;
    protected boolean fixedLength = false;
    protected int maxLength = -1;

    Setting pairedSetting;

    public IntArraySetting(IEnabler dependency, String name, String comment, int[] defaultValue, int min, int max) {
        super(dependency);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;

        pairedSetting = new ToggleSetting(
            dependency,
            "enable" + name.substring(0, 1)
                .toUpperCase() + name.substring(1),
            "Enable " + name + "?");
    }

    @Override
    public IntArraySetting setEnabled(boolean enabled) {
        pairedSetting.setEnabled(enabled);
        return this;
    }

    public int[] getValue() {
        return value;
    }

    @Override
    public boolean isEnabled() {
        return pairedSetting.isEnabled();
    }

    public IntArraySetting setLengthFixed(boolean isFixed) {
        this.fixedLength = isFixed;
        return this;
    }

    public IntArraySetting setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        pairedSetting.loadFromConfiguration(configuration);
        this.value = configuration
            .get(
                getCategory(),
                this.name,
                this.defaultValue,
                this.comment,
                this.minValue,
                this.maxValue,
                this.fixedLength,
                this.fixedLength ? this.defaultValue.length : this.maxLength)
            .getIntList();

        boolean wrong = false;
        for (int num : this.value) {
            if (num < this.minValue || num > this.maxValue) {
                SalisArcanaCore.LOG.error(
                    "Value {} in setting \"{}\" is out of bounds. (Bounds: {} <= n <= {})",
                    num,
                    this.name,
                    this.minValue,
                    this.maxValue);
                wrong = true;
            }
        }

        if (wrong) {
            if (this.fixedLength) {
                this.value = this.defaultValue;
            } else {
                this.value = Arrays.stream(this.value)
                    .filter(v -> (v >= this.minValue && v <= this.maxValue))
                    .toArray();
            }
        }
    }

}
