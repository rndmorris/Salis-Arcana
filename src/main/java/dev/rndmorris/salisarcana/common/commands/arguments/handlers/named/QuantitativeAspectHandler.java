package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new QuantitativeAspectHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var aspect = getAspect(current);

        current = "";
        if (args.hasNext()) {
            current = args.next();
        }
        final var amount = getAmount(sender, current);

        return new QuantitativeAspectArgument(aspect, amount);
    }

    private Aspect getAspect(String input) {
        if (input != null && !input.isEmpty()) {
            for (var kv : Aspect.aspects.entrySet()) {
                if (kv.getKey()
                    .equalsIgnoreCase(input)) {
                    return kv.getValue();
                }
            }
        }
        throw new CommandException("salisarcana:error.invalid_aspect", input);
    }

    private int getAmount(ICommandSender sender, String input) {
        return CommandBase.parseIntWithMin(sender, input, 1);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        if (!args.hasNext()) {
            return new ArrayList<>(Aspect.aspects.keySet());
        }
        args.next();
        if (!args.hasNext()) {
            return Collections.singletonList("1");
        }
        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return QuantitativeAspectArgument.class;
    }
}
