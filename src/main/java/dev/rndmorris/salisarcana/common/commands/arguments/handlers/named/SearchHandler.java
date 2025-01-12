package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class SearchHandler implements INamedArgumentHandler {

    public static final IArgumentHandler INSTANCE = new SearchHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> args) {
        return buildSearchTerm(args);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, PeekingIterator<String> args) {
        buildSearchTerm(args);
        if (!args.hasNext()) {
            return Collections.emptyList();
        }
        return null;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return String.class;
    }

    private String buildSearchTerm(PeekingIterator<String> args) {
        var insideQuote = false;
        var escapeNext = false;

        final var str = new StringBuilder();

        while (args.hasNext()) {
            if (!insideQuote && str.length() > 0) {
                break;
            }
            if (str.length() > 0) {
                str.append(' ');
            }
            final var currentTerm = args.next();

            for (var index = 0; index < currentTerm.length(); ++index) {
                final var character = currentTerm.charAt(index);
                if (character == '\\') {
                    if (escapeNext) {
                        str.append(character);
                        escapeNext = false;
                        continue;
                    }
                    escapeNext = true;
                    continue;
                }
                if (character == '"') {
                    if (escapeNext) {
                        str.append(character);
                        escapeNext = false;
                        continue;
                    }
                    if (insideQuote) {
                        insideQuote = false;
                        break;
                    }
                    insideQuote = true;
                    continue;
                }
                str.append(character);
            }
        }

        return str.toString();
    }

}
