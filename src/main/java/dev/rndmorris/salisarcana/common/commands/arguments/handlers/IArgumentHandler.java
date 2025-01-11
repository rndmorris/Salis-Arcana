package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

public interface IArgumentHandler {

    Object parse(ICommandSender sender, PeekingIterator<String> args);

    default List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        return null;
    };

    @Nonnull
    Class<?> getOutputType();
}
