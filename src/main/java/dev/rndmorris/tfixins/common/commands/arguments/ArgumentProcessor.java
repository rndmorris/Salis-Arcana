package dev.rndmorris.tfixins.common.commands.arguments;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.lib.ClassComparator;

public class ArgumentProcessor<TArguments> {

    private final Map<Class<? extends IArgumentHandler>, IArgumentHandler> argumentHandlers = new TreeMap<>(
        new ClassComparator());
    private final Class<TArguments> argumentsClass;
    private final Supplier<TArguments> initializer;

    private final Map<Integer, ArgEntry> positionalArgs = new TreeMap<>();
    private final Map<String, ArgEntry> namedArgs = new TreeMap<>();

    public ArgumentProcessor(Class<TArguments> argumentsClass, Supplier<TArguments> initializer,
        IArgumentHandler[] argumentHandlers) {
        this.argumentsClass = argumentsClass;
        this.initializer = initializer;
        for (var handler : argumentHandlers) {
            this.argumentHandlers.put(handler.getClass(), handler);
        }
        buildArgumentMaps();
    }

    public TArguments process(ICommandSender sender, String[] args) {

        final var excludedNames = new TreeSet<>(String::compareToIgnoreCase);
        final var arguments = initializer.get();

        final var $args = Arrays.stream(args)
            .iterator();
        var index = 0;

        while ($args.hasNext()) {
            final var current = $args.next();
            ArgEntry entry = null;
            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else if (namedArgs.containsKey(current)) {
                final var namedEntry = namedArgs.get(current);
                if (!namedEntry.isList || !excludedNames.contains(current)) {
                    excludedNames.add(current);
                    entry = namedEntry;
                }
                excludedNames.addAll(namedEntry.excludes);
            }

            if (entry == null) {
                throw new CommandException("tfixins:error.unexpected_value", current);
            }

            final var value = entry.parser.parse(sender, current, $args);
            entry.setter.accept(arguments, value);

            index += 1;
        }

        return arguments;
    }

    public List<String> getAutocompletionSuggestions(ICommandSender sender, String[] args) {
        final var excludedNames = new TreeSet<String>();

        final var $args = Arrays.stream(args)
            .iterator();
        var index = 0;

        while ($args.hasNext()) {
            final var current = $args.next();
            ArgEntry entry = null;
            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else if (namedArgs.containsKey(current)) {
                final var namedEntry = namedArgs.get(current);
                if (!namedEntry.isList || !excludedNames.contains(current)) {
                    excludedNames.add(current);
                    entry = namedEntry;
                }
                excludedNames.addAll(namedEntry.excludes);
            }

            if (entry == null) {
                return namedArgs.keySet()
                    .stream()
                    .filter(k -> !excludedNames.contains(k))
                    .collect(Collectors.toList());
            }

            final var result = entry.parser.getAutocompleteOptions(sender, current, $args);
            if (result != null) {
                return result;
            }

            index += 1;
        }

        return Collections.emptyList();
    }

    private void buildArgumentMaps() {
        for (var field : argumentsClass.getFields()) {
            field.setAccessible(true);

            final var entry = new ArgEntry();

            PositionalArg posArg;
            NamedArg namedArg;
            if ((posArg = field.getAnnotation(PositionalArg.class)) != null) {
                positionalArgs.put(posArg.index(), entry);
                entry.parser = argumentHandlers.get(posArg.parser());
                if (entry.parser == null) {
                    LOG.error(String.format("No parser found for positional argument at index %s", posArg.index()));
                    throw new CommandException("commands.generic.exception");
                }
            } else if ((namedArg = field.getAnnotation(NamedArg.class)) != null) {
                namedArgs.put(namedArg.name(), entry);
                entry.parser = argumentHandlers.get(namedArg.handler());
                if (entry.parser == null) {
                    throw new RuntimeException(String.format("No parser found for named argument %s", namedArg.name()));
                }
                entry.excludes = Arrays.stream(namedArg.excludes())
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            }

            final var fieldType = field.getType();
            entry.isList = List.class.isAssignableFrom(fieldType);

            if (entry.isList) {
                entry.setter = (arguments, val) -> {
                    try {
                        // ensure the field value is an initialized list
                        List<Object> list;
                        final var fieldObj = field.get(arguments);
                        if (fieldObj == null) {
                            list = new ArrayList<>();
                            field.set(arguments, list);
                        } else {
                            // noinspection unchecked
                            list = (List<Object>) fieldObj;
                        }

                        list.add(val);
                    } catch (IllegalAccessException e) {
                        LOG.error(e);
                    }
                };
            } else {
                entry.setter = (arguments, val) -> {
                    try {
                        field.set(arguments, val);
                    } catch (IllegalAccessException e) {
                        LOG.error(e);
                    }
                };
            }

        }
    }

    private static class ArgEntry {

        public IArgumentHandler parser;
        public BiConsumer<Object, Object> setter;
        public boolean isList;
        public List<String> excludes = Collections.emptyList();
    }
}
