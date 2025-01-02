package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new QuantitativeAspectHandler();

    @Override
    public Object parse(ICommandSender sender, String current, PeekingIterator<String> args) {
        final var result = new ArrayList<QuantitativeAspectArgument>();

        Aspect aspect = getAspect(current);
        int amount = getAmount(sender, args);
        result.add(new QuantitativeAspectArgument(aspect, amount));

        String peek;
        while (args.hasNext() && (peek = args.peek()) != null && !peek.startsWith("-")) {
            aspect = getAspect(args.next());
            amount = getAmount(sender, args);
            result.add(new QuantitativeAspectArgument(aspect, amount));
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

    private int getAmount(ICommandSender sender, PeekingIterator<String> args) {
        if (args.hasNext()) {
            return CommandBase.parseIntWithMin(sender, args.next(), 1);
        }
        throw new NumberInvalidException("commands.generic.num.invalid", "");
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, PeekingIterator<String> args) {
        final var foundAspects = new HashSet<String>();
        Aspect aspect = AspectHelper.findAspect(current);
        if (aspect == null) {
            return toSortedList(Aspect.aspects.keySet());
        }
        foundAspects.add(aspect.getTag());

        var justParsedAspect = true;

        String peek = null;
        while (args.hasNext() && (peek = args.peek()) != null && !peek.startsWith("-")) {
            if (justParsedAspect) {
                args.next();
                justParsedAspect = false;
                continue;
            }
            aspect = AspectHelper.findAspect(args.next());
            if (aspect != null) {
                foundAspects.add(aspect.getTag());
            }
            justParsedAspect = true;
        }

        if (peek != null && peek.startsWith("-")) {
            return null;
        }

        if (!args.hasNext() && justParsedAspect) {
            final var suggestAspects = new HashSet<>(Aspect.aspects.keySet());
            suggestAspects.removeAll(foundAspects);
            return toSortedList(suggestAspects);
        }
        if (!justParsedAspect) {
            return Collections.singletonList("1");
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
