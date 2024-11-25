package dev.rndmorris.tfixins.config;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

public class CommandsModule implements IConfigModule {

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Nonnull
    @Override
    public String getModuleId() {
        return "commands";
    }

    @Nonnull
    @Override
    public String getModuleComment() {
        return "Helper and admin commands.";
    }

    @Override
    public void loadModuleFromConfig(@Nonnull Configuration configuration) {

    }
}
