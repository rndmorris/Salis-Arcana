package dev.rndmorris.tfixins.config;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

public class ThaumonomiconModule implements IConfigModule {

    public final Setting scrollwheelEnabled;
    public final Setting invertedScrolling;

    private boolean enabled = true;

    public ThaumonomiconModule() {
        final Supplier<IConfigModule> getter = () -> this;
        scrollwheelEnabled = new ToggleSetting(
            getter,
            "Enable Scrollwheel",
            "Enables ctrl + scroll to quick switch tabs");
        invertedScrolling = new ToggleSetting(getter, "Inverse Scrolling", "Inverts the scrolling for tab switching")
            .setEnabled(false);
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "thaumonomicon";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Modifications to the Thaumonomicon's GUI";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {
        scrollwheelEnabled.loadFromConfiguration(configuration);
        invertedScrolling.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
