package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
    public Object parse(ICommandSender sender, String current, PeekingIterator<String> args) {
        final var result = new ArrayList<Aspect>();
        result.add(getAspect(current));
        String peek;
        while (args.hasNext() && (peek = args.peek()) != null && !peek.startsWith("-")) {
            result.add(getAspect(args.next()));
        }
        return result;
    }

    private Aspect getAspect(String input) {
        final var result = AspectHelper.findAspect(input);
        if (result == null) {
            throw new CommandException("salisarcana:error.invalid_aspect", input);
        }
        return result;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, PeekingIterator<String> args) {
        final var foundAspects = new HashSet<String>();
        Aspect aspect = AspectHelper.findAspect(current);
        if (aspect != null) {
            foundAspects.add(aspect.getTag());
        }
        String peek;
        while (args.hasNext() && (peek = args.peek()) != null && !peek.startsWith("-")) {
            aspect = AspectHelper.findAspect(args.next());
            if (aspect != null) {
                foundAspects.add(aspect.getTag());
            }
        }

        if (!args.hasNext()) {
            final var suggestAspects = new HashSet<>(Aspect.aspects.keySet());
            suggestAspects.removeAll(foundAspects);
            return toSortedList(suggestAspects);
        }
        return null;
    }

    private ArrayList<String> toSortedList(Collection<String> aspects) {
        final var result = new ArrayList<>(aspects);
        result.sort(String::compareToIgnoreCase);
        return result;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return List.class;
    }
}
