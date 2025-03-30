package dev.rndmorris.salisarcana.config.settings;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.IEnabler;

public class IntArraySetting extends Setting {

    protected final String name;
    protected final String comment;
    protected final int[] defaultValue;
    protected int[] value;
    protected int minValue;
    protected int maxValue;

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

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        pairedSetting.loadFromConfiguration(configuration);
        this.value = configuration
            .get(getCategory(), this.name, this.defaultValue, this.comment, this.minValue, this.maxValue)
            .getIntList();
    }

}
