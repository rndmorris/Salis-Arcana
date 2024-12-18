package dev.rndmorris.tfixins.config;

import java.lang.ref.WeakReference;

import net.minecraftforge.common.config.Configuration;

public class ToggleSetting extends Setting {

    private final String name;
    private final String comment;
    private final WeakReference<IConfigModule> parentModule;

    public ToggleSetting(IConfigModule module, ConfigPhase phase, String name, String comment) {
        super(module, phase);
        this.name = name;
        this.comment = comment;
        parentModule = new WeakReference<>(module);
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        final var module = parentModule.get();
        if (module != null) {
            enabled = configuration.getBoolean(name, module.getModuleId(), enabled, comment);
        }
    }
}
