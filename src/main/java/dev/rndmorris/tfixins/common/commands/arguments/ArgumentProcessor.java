package dev.rndmorris.tfixins.common.commands.arguments;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;

import java.lang.reflect.Field;
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
import java.util.stream.Stream;

import dev.rndmorris.tfixins.common.commands.CommandErrors;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.FlagArg;
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
    private final Map<String, ArgEntry> flagArgs = new TreeMap<>();
    private final Map<String, ArgEntry> namedArgs = new TreeMap<>();

    public final List<String> descriptionLangKeys = new ArrayList<>();

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
            var current = $args.next();
            ArgEntry entry = null;

            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else if (flagArgs.containsKey(current)) {
                entry = flagArgs.get(current);
            } else if (namedArgs.containsKey(current)) {
                entry = namedArgs.get(current);
            }

            if (entry != null && (entry.argType == ArgType.FLAG || entry.argType == ArgType.NAMED)) {
                if (excludedNames.contains(current)) {
                    entry = null;
                } else {
                    if (!entry.isList) {
                        excludedNames.add(current);
                    }
                    excludedNames.addAll(entry.excludes);
                }
            }

            if (entry == null) {
                throw new CommandException("tfixins:error.unexpected_value", current);
            }

            // pre-advance the iterator for named arguments, because they ALL need it
            if (entry.argType == ArgType.NAMED) {
                if ($args.hasNext()) {
                    current = $args.next();
                } else {
                    CommandErrors.invalidSyntax();
                }
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
            var current = $args.next();
            ArgEntry entry = null;

            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else if (flagArgs.containsKey(current)) {
                entry = flagArgs.get(current);
            } else if (namedArgs.containsKey(current)) {
                entry = namedArgs.get(current);
            }

            if (entry != null && (entry.argType == ArgType.FLAG || entry.argType == ArgType.NAMED)) {
                if (excludedNames.contains(current)) {
                    entry = null;
                } else {
                    if (!entry.isList) {
                        excludedNames.add(current);
                    }
                    excludedNames.addAll(entry.excludes);
                }
            }

            if (entry == null) {
                final var availableFlags = flagArgs.keySet()
                    .stream()
                    .filter(k -> !excludedNames.contains(k));
                final var availableNames = namedArgs.keySet()
                    .stream()
                    .filter(k -> !excludedNames.contains(k));
                return Stream.concat(availableFlags, availableNames)
                    .collect(Collectors.toList());
            }

            // pre-advance the iterator for named arguments, because they ALL need it
            if (entry.argType == ArgType.NAMED) {
                if ($args.hasNext()) {
                    current = $args.next();
                } else {
                    return Collections.emptyList();
                }
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

            if (!(evaluatePositionalArg(field, entry) || evaluateFlagArg(field, entry)
                || evaluateNamedArg(field, entry))) {
                continue;
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

    private boolean evaluatePositionalArg(Field field, ArgEntry entry) {
        var posArg = field.getAnnotation(PositionalArg.class);
        if (posArg == null) {
            return false;
        }
        positionalArgs.put(posArg.index(), entry);
        entry.parser = argumentHandlers.get(posArg.handler());
        if (entry.parser == null) {
            LOG.error(String.format("No parser found for positional argument at index %d", posArg.index()));
            throw new RuntimeException();
        }
        if (!posArg.descLangKey()
            .isEmpty()) {
            descriptionLangKeys.add(posArg.descLangKey());
        }
        entry.argType = ArgType.POS;

        return true;
    }

    private boolean evaluateFlagArg(Field field, ArgEntry entry) {
        var flagArg = field.getAnnotation(FlagArg.class);
        if (flagArg == null) {
            return false;
        }

        flagArgs.put(flagArg.name(), entry);
        entry.parser = argumentHandlers.get(flagArg.handler());
        if (entry.parser == null) {
            LOG.error(String.format("No parser found for named argument at %s", flagArg.name()));
            throw new RuntimeException();
        }
        if (!flagArg.descLangKey()
            .isEmpty()) {
            descriptionLangKeys.add(flagArg.descLangKey());
        }
        entry.excludes = Arrays.stream(flagArg.excludes())
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
        entry.argType = ArgType.FLAG;

        return true;
    }

    private boolean evaluateNamedArg(Field field, ArgEntry entry) {
        final var namedArg = field.getAnnotation(NamedArg.class);
        if (namedArg == null) {
            return false;
        }

        namedArgs.put(namedArg.name(), entry);
        entry.parser = argumentHandlers.get(namedArg.handler());
        if (entry.parser == null) {
            LOG.error(String.format("No parser found for named argument at %s", namedArg.name()));
            throw new RuntimeException();
        }
        if (!namedArg.descLangKey()
            .isEmpty()) {
            descriptionLangKeys.add(namedArg.descLangKey());
        }
        entry.excludes = Arrays.stream(namedArg.excludes())
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
        entry.argType = ArgType.NAMED;

        return true;
    }

    private static class ArgEntry {

        public IArgumentHandler parser;
        public BiConsumer<Object, Object> setter;
        public boolean isList;
        public List<String> excludes = Collections.emptyList();
        public ArgType argType;
    }

    private enum ArgType {
        POS,
        FLAG,
        NAMED;
    }
}
