package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import dev.rndmorris.salisarcana.lib.IntegerHelper;
import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new QuantitativeAspectHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var result = new TreeSet<>(QuantitativeAspectArgument.COMPARATOR);

        String peeked;
        do {
            final var tag = args.hasNext() ? args.next() : "";
            final var aspect = AspectHelper.aspectsCI()
                .get(tag);
            if (aspect == null) {
                throw new CommandException("salisarcana:error.invalid_aspect", tag);
            }
            final var amountStr = args.hasNext() ? args.next() : "";
            final var amount = CommandBase.parseIntWithMin(sender, amountStr, 1);
            result.add(new QuantitativeAspectArgument(aspect, amount));
        } while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-"));

        return new ArrayList<>(result);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        final var alreadyFound = new TreeSet<>(QuantitativeAspectArgument.COMPARATOR);

        String peeked;
        do {
            if (!args.hasNext()) {
                return aspectSuggestions(alreadyFound);
            }
            final var tag = args.next();
            final var aspect = AspectHelper.aspectsCI()
                .get(tag);

            if (aspect == null) {
                return aspectSuggestions(alreadyFound);
            }

            if (!args.hasNext()) {
                return Collections.singletonList("1");
            }
            final var amountStr = args.next();
            final var amount = IntegerHelper.tryParseWithMin(amountStr, 1);

            if (amount == null) {
                return Collections.singletonList("1");
            }

            alreadyFound.add(new QuantitativeAspectArgument(aspect, amount));
        } while (args.hasNext() && (peeked = args.peek()) != null && !peeked.startsWith("-"));

        return null;
    }

    private List<String> aspectSuggestions(Set<QuantitativeAspectArgument> except) {
        return new ArrayList<>(
            AspectHelper.aspectsExcept(
                except.stream()
                    .map(a -> a.aspect)
                    .collect(Collectors.toList()))).stream()
                        .map(Aspect::getTag)
                        .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return List.class;
    }
}
