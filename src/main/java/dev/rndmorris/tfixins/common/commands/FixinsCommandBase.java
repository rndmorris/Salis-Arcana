package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.config.CommandsModule.CommandSettings;
import net.minecraft.command.ICommandSender;

public abstract class FixinsCommandBase extends CommandBase {

    protected CommandSettings settings;

    public FixinsCommandBase(@Nonnull CommandSettings settings) {
        this.settings = settings;
    }

    @Override
    public String getCommandName() {
        return String.format("%s-%s", ThaumicFixins.MODID, settings.name);
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(settings.aliases);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.format("%s:command.%s.usage", ThaumicFixins.MODID, settings.name);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return settings.getPermissionLevel();
    }
}
