package dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.CommandErrors;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class CommandNameHandler implements IPositionalArgumentHandler {

    public static final IArgumentHandler INSTANCE = new CommandNameHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var command = findCommand(args.next());
        if (command == null) {
            CommandErrors.commandNotFound();
        }
        if (!sender.canCommandSenderUseCommand(command.getPermissionLevel(), command.getFullName())) {
            CommandErrors.insufficientPermission();
        }
        return command;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
        final var settingsList = SalisConfig.commands.getCommandsSettings()
            .collect(Collectors.toList());

        if (!args.hasNext()) {
            // automatically sort our output alphanumerically
            final var results = new TreeSet<>(String::compareToIgnoreCase);
            for (var settings : settingsList) {
                if (!settings.isEnabled()) {
                    continue;
                }
                Collections.addAll(results, settings.getFullName());
                results.addAll(settings.aliases);
            }
            return new ArrayList<>(results);
        }

        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return CommandSettings.class;
    }

    private CommandSettings findCommand(final String current) {
        var maybeFoundCommand = SalisConfig.commands.getCommandsSettings()
            .filter(CommandSettings::isEnabled)
            .filter(
                s -> s.getFullName()
                    .equalsIgnoreCase(current)
                    || s.aliases.stream()
                        .anyMatch(a -> a.equalsIgnoreCase(current)))
            .limit(1)
            .collect(Collectors.toList());
        return !maybeFoundCommand.isEmpty() ? maybeFoundCommand.get(0) : null;
    }
}
