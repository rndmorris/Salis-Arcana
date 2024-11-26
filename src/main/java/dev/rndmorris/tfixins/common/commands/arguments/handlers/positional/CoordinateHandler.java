package dev.rndmorris.tfixins.common.commands.arguments.handlers.positional;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.CommandErrors;
import dev.rndmorris.tfixins.common.commands.arguments.CoordinateArgument;

public class CoordinateHandler implements IPositionalArgumentHandler {

    public static final IArgumentHandler INSTANCE = new CoordinateHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);

        final var x = (int) CommandBase.func_110666_a(sender, player.posX, current);

        if (!input.hasNext()) {
            CommandErrors.invalidSyntax();
        }
        final var y = (int) CommandBase.func_110666_a(sender, player.posY, input.next());

        if (!input.hasNext()) {
            CommandErrors.invalidSyntax();
        }
        final var z = (int) CommandBase.func_110666_a(sender, player.posZ, input.next());

        return new CoordinateArgument(x, y, z);
    }

    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        final var tilde = Collections.singletonList("~");
        if (!args.hasNext()) {
            return tilde;
        }
        args.next();
        if (!args.hasNext()) {
            return tilde;
        }
        args.next();
        if (!args.hasNext()) {
            return tilde;
        }

        return null;
    }
}
