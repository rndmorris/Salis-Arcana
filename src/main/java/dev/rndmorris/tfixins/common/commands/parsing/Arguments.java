package dev.rndmorris.tfixins.common.commands.parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class Arguments {

    private final Map<Integer, Entry> positional = new TreeMap<>();
    private final Map<String, Entry> named = new TreeMap<>();

    public void addFlag(String name, Parser parser) {
        named.put(name, new Entry(null, parser, 1));
    }

    public void addNamed(String name, Parser parser, AutoComplete autoComplete) {
        named.put(name, new Entry(autoComplete, parser));
    }

    public void addNamed(String name, Parser parser, AutoComplete autoComplete, int maxCount) {
        named.put(name, new Entry(autoComplete, parser, maxCount));
    }

    public void addPositional(int index, Parser parser, AutoComplete autoComplete) {
        positional.put(index, new Entry(autoComplete, parser));
    }

    public void parse(ICommandSender sender, String[] args) {
        final var namedCounts = new TreeMap<String, Integer>();
        Parser currentParser = null;

        for (var index = 0; index < args.length; ++index) {
            final var arg = args[index];

            if (currentParser != null) {
                currentParser = currentParser.invoke(arg);
                continue;
            }

            if (positional.containsKey(index)) {
                currentParser = positional.get(index).parser.invoke(arg);
                continue;
            }

            if (named.containsKey(arg)) {
                final var entry = named.get(arg);
                final var storedCount = namedCounts.get(arg);
                final var count = storedCount != null ? storedCount : 0;

                if (count > entry.maxCount) {
                    throw new CommandException("tfixins:command.too_many_occurrences", arg, entry.maxCount);
                }

                namedCounts.put(arg, count + 1);

                currentParser = named.get(arg).parser.invoke(arg);

                continue;
            }

            throw new CommandException(String.format("Unhandled argument \"%s\"", arg));
        }
    }

    public Collection<String> autocomplete(String[] args) {
        final var namedCounts = new TreeMap<String, Integer>();
        AutoComplete currentAutoComplete = null;
        ArrayList<String> result = null;

        for (var index = 0; index < args.length; ++index) {
            final var arg = args[index];

            if (index + 1 >= args.length) {
                result = new ArrayList<>();
            }

            if (currentAutoComplete != null) {
                currentAutoComplete = currentAutoComplete.invoke(arg, result);
                if (result != null) {
                    return result;
                }
                continue;
            }

            if (positional.containsKey(index)) {
                final var nextAuto = positional.get(index).autoComplete;
                if (nextAuto != null) {
                    currentAutoComplete = nextAuto.invoke(arg, result);
                }
                if (result != null) {
                    return result;
                }
                continue;
            }

            if (named.containsKey(arg)) {
                final var entry = named.get(arg);
                final var storedCount = namedCounts.get(arg);
                final var count = storedCount != null ? storedCount : 0;

                if (entry.maxCount < count) {
                    continue;
                }
                namedCounts.put(arg, count + 1);
                currentAutoComplete = named.get(arg).autoComplete;
            }
        }

        return named.entrySet()
            .stream()
            .filter(e -> {
                final var storedCount = namedCounts.get(e.getKey());
                final var count = storedCount != null ? storedCount : 0;
                return count < e.getValue().maxCount;
            })
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private static class Entry {

        public AutoComplete autoComplete;
        public Parser parser;
        public int maxCount = Integer.MAX_VALUE;

        public Entry(AutoComplete autoComplete, Parser parser) {
            this.autoComplete = autoComplete;
            this.parser = parser;
        }

        public Entry(AutoComplete autoComplete, Parser parser, int maxCount) {
            this(autoComplete, parser);
            this.maxCount = maxCount;
        }
    }

    public interface Parser {

        @Nullable
        Parser invoke(String value);
    }

    public interface AutoComplete {

        @Nullable
        AutoComplete invoke(String value, List<String> result);
    }
}
