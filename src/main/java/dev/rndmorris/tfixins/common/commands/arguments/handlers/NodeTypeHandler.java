package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.NodeTypeArgument;
import dev.rndmorris.tfixins.lib.EnumHelper;

public class NodeTypeHandler implements IArgumentHandler {

    public static final IArgumentHandler INSTANCE = new NodeTypeHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        NodeTypeArgument result = null;

        if (args.hasNext()) {
            final var typeName = args.next();
            result = EnumHelper.tryParseEnum(NodeTypeArgument.values(), typeName);
        }

        if (result == null) {
            throw new CommandException("tfixins:error.invalid_node_type");
        }
        return result;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            args.next();
            if (!args.hasNext()) {
                return Arrays.stream(NodeTypeArgument.values())
                    .map(NodeTypeArgument::toString)
                    .collect(Collectors.toList());
            }
        }
        return null;
    }
}
