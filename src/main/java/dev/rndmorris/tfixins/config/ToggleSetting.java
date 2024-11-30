package dev.rndmorris.tfixins.config;

import net.minecraftforge.common.config.Configuration;

import java.util.function.Supplier;

public class ToggleSetting extends Setting {
    private final String name;
    private final String comment;
    private final boolean defaultValue;

    public ToggleSetting(Supplier<IConfigModule> getModule, String name, String comment, boolean defaultValue) {
        super(getModule);
        this.name = name;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    public ToggleSetting(Supplier<IConfigModule> getModule, String name, String comment) {
        this(getModule, name, comment, true);
    }

    @Override
    public void loadFromConfiguration(Configuration configuration) {
        enabled = configuration.getBoolean(name, parentModule.get().getModuleId(), defaultValue, comment);
    }
}
