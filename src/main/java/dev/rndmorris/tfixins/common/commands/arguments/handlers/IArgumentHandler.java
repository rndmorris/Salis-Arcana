package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

public interface IArgumentHandler {

    Object parse(ICommandSender sender, String current, Iterator<String> args);

    default List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        return null;
    };

    @Nonnull
    Class<?> getOutputType();
}
