package dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.MathHelper;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.CommandErrors;
import dev.rndmorris.salisarcana.common.commands.arguments.CoordinateArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class CoordinateHandler implements IPositionalArgumentHandler {

    public static final IArgumentHandler INSTANCE = new CoordinateHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var player = CommandBase.getCommandSenderAsPlayer(sender);

        final var x = parsePosition(sender, player.posX, args.next());

        if (!args.hasNext()) {
            CommandErrors.invalidSyntax();
        }

        final var y = parsePosition(sender, player.posY, args.next());

        if (!args.hasNext()) {
            CommandErrors.invalidSyntax();
        }
        final var z = parsePosition(sender, player.posZ, args.next());

        return new CoordinateArgument(x, y, z);
    }

    private int parsePosition(ICommandSender sender, double playerPosition, String value) {
        return MathHelper.floor_double(CommandBase.func_110666_a(sender, playerPosition, value));
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        final var tilde = Collections.singletonList("~");
        args.next();
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

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return CoordinateArgument.class;
    }
}
