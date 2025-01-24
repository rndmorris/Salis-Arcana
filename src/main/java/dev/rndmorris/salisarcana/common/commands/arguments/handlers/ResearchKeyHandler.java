package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.StatCollector;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import thaumcraft.api.research.ResearchCategories;

public class ResearchKeyHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ResearchKeyHandler INSTANCE = new ResearchKeyHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var current = args.next();
        final var results = allResearchKeys().filter(current::equals)
            .collect(Collectors.toList());
        if (results.isEmpty()) {
            throw new CommandException("salisarcana:error.unknown_research", current);
        }

        return results.get(0);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        args.next();
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
        List<String> keys = new ArrayList<>();
        for (final var category : ResearchCategories.researchCategories.values()) {
            for (final var research : category.research.values()) {
                // Thaumcraft keys are already localized, but some mods (eg. Thaumic Tinkerer) may override getName()
                // and use their own localization prefix, not tc.research_name., so we need to localize them again
                // This doesn't affect any already-localized keys, as translateToLocal() will fail and just return
                // the input string.
                keys.add(StatCollector.translateToLocal(research.key));
            }
        }
        return keys.stream();
    }
}
