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

    public IntArraySetting(WeakReference<IConfigModule> getModule, String name, String comment, int[] defaultValue,
        int min, int max, boolean enabled) {
        super(getModule);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;
        this.enabled = enabled;

        pairedSetting = new ToggleSetting(
            getModule,
            "enable" + name.substring(0, 1)
                .toUpperCase() + name.substring(1),
            "Enable " + name + "?",
            enabled);
    }

    public IntArraySetting(WeakReference<IConfigModule> getModule, String name, String comment) {
        this(getModule, name, comment, new int[] {}, 0, 0, true);
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
