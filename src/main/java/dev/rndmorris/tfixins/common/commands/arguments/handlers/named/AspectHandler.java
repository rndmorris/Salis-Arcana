package dev.rndmorris.tfixins.common.commands.arguments.handlers.named;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import thaumcraft.api.aspects.Aspect;

public class AspectHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new AspectHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        current = "";
        if (args.hasNext()) {
            current = args.next();
        }
        return getAspect(current);
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

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            args.next();
            if (!args.hasNext()) {
                return new ArrayList<>(Aspect.aspects.keySet());
            }
        }

        return null;
    }
}
