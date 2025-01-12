package dev.rndmorris.salisarcana.common.commands.arguments.handlers;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class ResearchHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ResearchHandler INSTANCE = new ResearchHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        final var result = ResearchKeyHandler.INSTANCE.parse(sender, args);
        if (result instanceof String key) {
            return ResearchCategories.getResearch(key);
        }
        return null;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        return ResearchKeyHandler.INSTANCE.getAutocompleteOptions(sender, args);
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return ResearchItem.class;
    }
}
