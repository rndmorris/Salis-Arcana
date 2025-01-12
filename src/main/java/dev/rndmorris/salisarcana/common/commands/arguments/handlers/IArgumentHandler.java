package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

public interface IArgumentHandler {

    /**
     * Read {@code args} to get the desired output. The return value should be assignable to
     * {@link IArgumentHandler#getOutputType()}.
     * 
     * @param sender The player executing the command
     * @param args   All command args yet to be processed. Should *always* be given at least one next value.
     * @return The parsed value
     */
    Object parse(ICommandSender sender, PeekingIterator<String> args);

    /**
     * Read {@code args} to process inputs and return any tab-completion values the user might need.
     * 
     * @param sender The player executing the command
     * @param args   All command args yet to be processed. Should *always* be given at least one next value.
     * @return Tab-completion suggestions, or null if this handler is satisfied and the next handler can be checked.
     */
    default List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        return null;
    };

    /**
     * The return type of {@link IArgumentHandler#parse(ICommandSender, PeekingIterator)}. Should be {@link List} if
     * {@link IArgumentHandler#parse(ICommandSender, PeekingIterator)} can return multiple objects.
     */
    @Nonnull
    Class<?> getOutputType();
}
