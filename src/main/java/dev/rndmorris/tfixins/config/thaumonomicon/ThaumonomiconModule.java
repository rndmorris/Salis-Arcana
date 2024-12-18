package dev.rndmorris.tfixins.config.thaumonomicon;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.tfixins.config.ConfigPhase;
import dev.rndmorris.tfixins.config.IConfigModule;
import dev.rndmorris.tfixins.config.Setting;
import dev.rndmorris.tfixins.config.ToggleSetting;

public class ThaumonomiconModule implements IConfigModule {

    public final Setting scrollwheelEnabled;
    public final Setting invertedScrolling;
    public final Setting rightClickClose;
    public final Setting showResearchId;

    private boolean enabled = true;

    private final Setting[] settings;

    public ThaumonomiconModule() {
        settings = new Setting[] {
            this.invertedScrolling = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Inverse Scrolling",
                "Inverts the scrolling for tab switching").setEnabled(false),
            rightClickClose = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Right-Click Navigation",
                "Right clicking in a research will take you back to the previous research, or back to the Thaumonomicon."),
            scrollwheelEnabled = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Enable Scrollwheel",
                "Enables ctrl + scroll to quick switch tabs"),
            showResearchId = new ToggleSetting(
                this,
                ConfigPhase.EARLY,
                "Show Research Key",
                "Allows you to view the internal name of a research while hovering over it and holding control"), };
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
    public void loadModuleFromConfig(@Nonnull Configuration configuration, ConfigPhase phase) {
        for (Setting setting : settings) {
            if (setting.phase == phase) {
                setting.loadFromConfiguration(configuration);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
