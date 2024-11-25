package dev.rndmorris.tfixins.common.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.tfixins.ThaumicFixins;
import dev.rndmorris.tfixins.config.CommandsModule.CommandSettings;

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

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (hasSpecialFlag(sender, args)) {
            return;
        }
        process(sender, args);
    }

    protected boolean hasSpecialFlag(ICommandSender sender, String[] args) {
        for (var arg : args) {
            final var lowerArg = arg.toLowerCase();

            if ("--aliases".equalsIgnoreCase(lowerArg)) {
                printAliases(sender);
                return true;
            }

            if ("--help".equalsIgnoreCase(lowerArg)) {
                printHelp(sender);
                return true;
            }
        }

        return false;
    }

    private void printAliases(ICommandSender sender) {
        if (settings.aliases.isEmpty()) {
            sender.addChatMessage(new ChatComponentTranslation("tfixins:command.aliases_none", getCommandName()));
            return;
        }

        sender.addChatMessage(new ChatComponentTranslation("tfixins:command.aliases", getCommandName()));
        for (var alias : settings.aliases) {
            sender.addChatMessage(new ChatComponentText(String.format("  /%s", alias)));
        }
    }

    protected void printHelp(ICommandSender sender) {}

    protected abstract void process(ICommandSender sender, String[] args);
}
