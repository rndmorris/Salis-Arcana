package dev.rndmorris.tfixins.config.thaumonomicon;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;

import java.util.function.Supplier;

public class ScrollwheelEnabled extends Setting {
    public ScrollwheelEnabled(Supplier<IConfigModule> getModule) {
        super(getModule);
    }
}
