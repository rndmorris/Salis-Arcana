package dev.rndmorris.tfixins.common.commands.arguments.handlers.named;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.lib.EnumHelper;

public abstract class EnumHandler<E extends Enum<E>> implements INamedArgumentHandler {

    protected final Class<E> enumDefinition;

    public EnumHandler(Class<E> enumDefinition) {
        this.enumDefinition = enumDefinition;
    }

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {

        final var result = EnumHelper.tryParseEnum(enumDefinition.getEnumConstants(), current);
        if (result == null) {
            throw new CommandException("tfixins:error.unknown_value", current);
        }

        return result;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            return null;
        }
        return Arrays.stream(enumDefinition.getEnumConstants())
            .map(E::toString)
            .collect(Collectors.toList());
    }
}
