package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.aspects.Aspect;

public class AspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new AspectHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var result = new TreeSet<>(AspectHelper.COMPARATOR);

        String peeked;
        do {
            final var tag = args.hasNext() ? args.next() : "";
            final var aspect = AspectHelper.aspectsCI()
                .get(tag);
            if (aspect == null) {
                throw new CommandException("salisarcana:error.invalid_aspect", tag);
            }
            result.add(aspect);
        } while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-"));

        return new ArrayList<>(result);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        final var alreadyIncluded = new TreeSet<>(AspectHelper.COMPARATOR);

        String peeked = null;
        do {
            final var tag = args.next();
            final var aspect = AspectHelper.aspectsCI()
                .get(tag);
            if (aspect == null) {
                continue;
            }
            alreadyIncluded.add(aspect);
        } while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-"));

        if (!args.hasNext() || (peeked != null && !peeked.startsWith("-"))) {
            return AspectHelper.aspectsExcept(alreadyIncluded)
                .stream()
                .map(Aspect::getTag)
                .collect(Collectors.toList());
        }

        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return List.class;
    }
}
