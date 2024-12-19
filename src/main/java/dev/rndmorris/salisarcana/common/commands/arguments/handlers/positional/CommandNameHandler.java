package dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.CommandErrors;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class CommandNameHandler implements IPositionalArgumentHandler {

    public static final IArgumentHandler INSTANCE = new CommandNameHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        final var command = findCommand(current);
        if (command == null) {
            CommandErrors.commandNotFound();
        }
        if (!sender.canCommandSenderUseCommand(command.getPermissionLevel(), command.getFullName())) {
            CommandErrors.insufficientPermission();
        }
        return command;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        final var settingsArr = ConfigModuleRoot.commands.commandsSettings;
        if (!args.hasNext()) {
            final var results = new ArrayList<String>(settingsArr.length * 2);
            for (var settings : settingsArr) {
                if (!settings.isEnabled()) {
                    continue;
                }
                Collections.addAll(results, settings.getFullName(), settings.name);
                results.addAll(settings.aliases);
            }

            return results;
        }

        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return CommandSettings.class;
    }

    private CommandSettings findCommand(String current) {
        CommandSettings foundCommand = null;
        for (var settings : ConfigModuleRoot.commands.commandsSettings) {
            if (!settings.isEnabled()) {
                continue;
            }
            if (settings.name.equalsIgnoreCase(current) || settings.getFullName()
                .equalsIgnoreCase(current)) {
                foundCommand = settings;
                break;
            }
            for (var alias : settings.aliases) {
                if (alias.equalsIgnoreCase(current)) {
                    foundCommand = settings;
                    break;
                }
            }
        }
        return foundCommand;
    }
}
