package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.NodeModifierArgument;
import dev.rndmorris.tfixins.lib.EnumHelper;

public class NodeModifierHandler implements IArgumentHandler {

    public static final IArgumentHandler INSTANCE = new NodeModifierHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        NodeModifierArgument result = null;

        if (args.hasNext()) {
            final var typeName = args.next();
            result = EnumHelper.tryParseEnum(NodeModifierArgument.values(), typeName);
        }

        if (result == null) {
            throw new CommandException("");
        }
        return result;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            args.next();
            if (!args.hasNext()) {
                return Arrays.stream(NodeModifierArgument.values())
                    .map(NodeModifierArgument::toString)
                    .collect(Collectors.toList());
            }
        }
        return null;
    }
}
