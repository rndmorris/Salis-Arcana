package dev.rndmorris.tfixins.config;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

public class IntArraySetting extends Setting {

    protected final String name;
    protected final String comment;
    protected final int[] defaultValue;
    protected int[] value;
    protected int minValue;
    protected int maxValue;

    Setting pairedSetting;

    public IntArraySetting(WeakReference<IConfigModule> getModule, ConfigPhase phase, String name, String comment,
        int[] defaultValue, int min, int max) {
        super(getModule, phase);
        this.name = name;
        this.phase = phase;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;

        pairedSetting = new ToggleSetting(
            getModule,
            phase,
            "enable" + name.substring(0, 1)
                .toUpperCase() + name.substring(1),
            "Enable " + name + "?",
            enabled);
    }

    public IntArraySetting(WeakReference<IConfigModule> getModule, ConfigPhase phase, String name, String comment) {
        this(getModule, phase, name, comment, new int[] {}, 0, 0);
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
        IConfigModule module;
        if ((module = moduleRef.get()) == null) {
            return;
        }
        this.value = configuration
            .get(module.getModuleId(), this.name, this.defaultValue, this.comment, this.minValue, this.maxValue)
            .getIntList();
    }

}
