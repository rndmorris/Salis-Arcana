package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.lib.EnumHelper;

public abstract class EnumHandler<E extends Enum<E>> implements INamedArgumentHandler {

    protected final Class<E> enumDefinition;

    public EnumHandler(Class<E> enumDefinition) {
        this.enumDefinition = enumDefinition;
    }

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {

        final var result = EnumHelper.tryParseEnum(enumDefinition.getEnumConstants(), args.next());
        if (result == null) {
            throw new CommandException("salisarcana:error.unknown_value", args.next());
        }

        return result;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
        if (args.hasNext()) {
            return null;
        }
        return Arrays.stream(enumDefinition.getEnumConstants())
            .map(E::toString)
            .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return enumDefinition;
    }
}
