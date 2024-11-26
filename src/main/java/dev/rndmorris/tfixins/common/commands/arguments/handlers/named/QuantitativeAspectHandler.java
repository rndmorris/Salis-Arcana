package dev.rndmorris.tfixins.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.QuantitativeAspectArgument;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import thaumcraft.api.aspects.Aspect;

public class QuantitativeAspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new QuantitativeAspectHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {

        current = "";
        if (args.hasNext()) {
            current = args.next();
        }
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
        throw new CommandException("tfixins:error.invalid_aspect", input);
    }

    private int getAmount(ICommandSender sender, String input) {
        return CommandBase.parseIntWithMin(sender, input, 1);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            args.next();
            if (!args.hasNext()) {
                return new ArrayList<>(Aspect.aspects.keySet());
            }
            args.next();
            if (!args.hasNext()) {
                return Collections.singletonList("1");
            }
        }

        return null;
    }
}
