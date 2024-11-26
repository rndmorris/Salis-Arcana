package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.config.CommandSettings;

public abstract class FixinsCommandBase extends CommandBase {

    protected CommandSettings settings;

    public FixinsCommandBase(@Nonnull CommandSettings settings) {
        settings.setCommandGetter(() -> this);
        this.settings = settings;
    }

    @Override
    public String getCommandName() {
        return settings.getFullName();
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

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        process(sender, args);
    }

    public void printHelp(ICommandSender sender) {}

    protected abstract void process(ICommandSender sender, String[] args);
}
