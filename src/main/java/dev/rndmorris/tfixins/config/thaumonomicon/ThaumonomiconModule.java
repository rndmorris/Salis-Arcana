package dev.rndmorris.tfixins.config.thaumonomicon;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;
import dev.rndmorris.tfixins.config.ToggleSetting;

public class ThaumonomiconModule implements IConfigModule {

    public final Setting scrollwheelEnabled;
    public final Setting invertedScrolling;
    public final Setting rightClickClose;
    public final Setting showResearchId;

    private boolean enabled = true;

    public ThaumonomiconModule() {
        final var thisRef = new WeakReference<IConfigModule>(this);
        invertedScrolling = new ToggleSetting(thisRef, "Inverse Scrolling", "Inverts the scrolling for tab switching")
            .setEnabled(false);
        rightClickClose = new ToggleSetting(
            thisRef,
            "Right-Click Navigation",
            "Right clicking in a research will take you back to the previous research, or back to the Thaumonomicon.");
        scrollwheelEnabled = new ToggleSetting(
            thisRef,
            "Enable Scrollwheel",
            "Enables ctrl + scroll to quick switch tabs");
        showResearchId = new ToggleSetting(
            thisRef,
            "Show Research Key",
            "Allows you to view the internal name of a research while hovering over it and holding control");
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
        rightClickClose.loadFromConfiguration(configuration);
        showResearchId.loadFromConfiguration(configuration);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
