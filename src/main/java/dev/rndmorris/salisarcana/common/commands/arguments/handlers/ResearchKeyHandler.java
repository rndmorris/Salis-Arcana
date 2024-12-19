package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import thaumcraft.api.research.ResearchCategories;

public class ResearchKeyHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ResearchKeyHandler INSTANCE = new ResearchKeyHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        final var results = allResearchKeys().filter(current::equals)
            .collect(Collectors.toList());
        if (results.isEmpty()) {
            throw new CommandException("salisarcana:error.unknown_research", current);
        }

        return results.get(0);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            return null;
        }
        return allResearchKeys().collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return String.class;
    }

    private Stream<String> allResearchKeys() {
        return ResearchCategories.researchCategories.values()
            .stream()
            .flatMap(
                c -> c.research.keySet()
                    .stream());
    }
}
