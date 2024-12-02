package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class ResearchHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final ResearchHandler INSTANCE = new ResearchHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        final var result = ResearchKeyHandler.INSTANCE.parse(sender, current, args);
        if (result instanceof String key) {
            return ResearchCategories.getResearch(key);
        }
        return null;
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        return ResearchKeyHandler.INSTANCE.getAutocompleteOptions(sender, current, args);
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return ResearchItem.class;
    }
}
