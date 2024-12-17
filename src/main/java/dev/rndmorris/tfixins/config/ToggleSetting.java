package dev.rndmorris.tfixins.config;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

public class ToggleSetting extends Setting {

    private final String name;
    private final String comment;
    private final boolean defaultValue;

    public ToggleSetting(WeakReference<IConfigModule> getModule, ConfigPhase phase, String name, String comment,
        boolean defaultValue) {
        super(getModule, phase);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    public ToggleSetting(WeakReference<IConfigModule> getModule, ConfigPhase phase, String name, String comment) {
        this(getModule, phase, name, comment, true);
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        IConfigModule module;
        if ((module = moduleRef.get()) != null) {
            enabled = configuration.getBoolean(name, module.getModuleId(), defaultValue, comment);
        }
    }
}
