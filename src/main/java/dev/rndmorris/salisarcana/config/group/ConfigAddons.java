package dev.rndmorris.salisarcana.config.group;

import org.jetbrains.annotations.NotNull;

import dev.rndmorris.salisarcana.config.ConfigGroup;
import dev.rndmorris.salisarcana.config.settings.ToggleSetting;

public class ConfigAddons extends ConfigGroup {

    public final ToggleSetting automagyBoilerFakePlayer = new ToggleSetting(
        this,
        "automagyBoilerFakePlayer",
        "Prevents a crash when using the Automagy boiler with fake players, like Autonomous Activators.")
            .setCategory("automagy");

    @Override
    public @NotNull String getGroupName() {
        return "addons";
    }

    @Override
    public @NotNull String getGroupComment() {
        return "Fixes for miscellaneous Thaumcraft addons.";
    }
}
