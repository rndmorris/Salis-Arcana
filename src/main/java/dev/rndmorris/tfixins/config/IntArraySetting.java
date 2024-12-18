package dev.rndmorris.tfixins.config;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

public class IntArraySetting extends Setting {

    private final WeakReference<IConfigModule> parentModule;

    protected final String name;
    protected final String comment;
    protected final int[] defaultValue;
    protected int[] value;
    protected int minValue;
    protected int maxValue;

    Setting pairedSetting;

    public IntArraySetting(IConfigModule module, ConfigPhase phase, String name, String comment, int[] defaultValue,
        int min, int max) {
        super(module, phase);
        this.name = name;
        this.phase = phase;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;

        parentModule = new WeakReference<>(module);

        pairedSetting = new ToggleSetting(
            module,
            phase,
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
        final var module = parentModule.get();
        if (module == null) {
            return;
        }
        this.value = configuration
            .get(module.getModuleId(), this.name, this.defaultValue, this.comment, this.minValue, this.maxValue)
            .getIntList();
    }

}
