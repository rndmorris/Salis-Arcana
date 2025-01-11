package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import thaumcraft.api.aspects.Aspect;

public class AspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new AspectHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        return getAspect(args.next());
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

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
        if (!args.hasNext()) {
            return new ArrayList<>(Aspect.aspects.keySet());
        }

        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return Aspect.class;
    }
}
