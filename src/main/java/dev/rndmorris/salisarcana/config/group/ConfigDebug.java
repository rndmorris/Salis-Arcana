package dev.rndmorris.salisarcana.config.group;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class ConfigDebug extends ConfigGroup {

    public final ToggleSetting logWarpSources = new ToggleSetting(
        this,
        "logWarpSources",
        "Print out the thread info, stack trace, player name, warp type, and quantity of warp applied whenever a player's warp changes.")
            .setEnabled(false);

    @Override
    public @NotNull String getGroupName() {
        return "debug";
    }

    @Override
    public @NotNull String getGroupComment() {
        return "Options for debugging Thaumcraft & its add-ons. All features in this group are disabled by default.";
    }
}
