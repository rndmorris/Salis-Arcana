package dev.rndmorris.salisarcana.common.commands.arguments;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.CommandErrors;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.FlagArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.lib.ClassComparator;
import dev.rndmorris.salisarcana.lib.PeekableIterator;

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

        final var $args = new PeekableIterator<>(
            Arrays.stream(args)
                .iterator());
        var index = 0;

        while ($args.hasNext()) {
            String current = null;
            ArgEntry entry = null;

            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else {
                current = $args.next();
                if (flagArgs.containsKey(current)) {
                    entry = flagArgs.get(current);
                } else if (namedArgs.containsKey(current)) {
                    entry = namedArgs.get(current);
                }
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
                throw new CommandException("salisarcana:error.unexpected_value", current);
            }

            // all named args will require at least one value
            if (entry.argType == ArgType.NAMED && !$args.hasNext()) {
                CommandErrors.invalidSyntax();
            }

            final var value = entry.handler.parse(sender, $args);
            entry.fieldSetter.accept(arguments, value);

            index += 1;
        }

        return arguments;
    }

    public List<String> getAutocompletionSuggestions(ICommandSender sender, String[] args) {
        final var excludedNames = new TreeSet<String>();

        final var $args = new PeekableIterator<>(
            Arrays.stream(args)
                .iterator());
        var index = 0;

        while ($args.hasNext()) {
            String current = null;
            ArgEntry entry = null;

            if (positionalArgs.containsKey(index)) {
                entry = positionalArgs.get(index);
            } else {
                current = $args.next();
                if (flagArgs.containsKey(current)) {
                    entry = flagArgs.get(current);
                } else if (namedArgs.containsKey(current)) {
                    entry = namedArgs.get(current);
                }
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

            if (entry == null || !$args.hasNext()) {
                final var availableFlags = flagArgs.keySet()
                    .stream()
                    .filter(k -> !excludedNames.contains(k));
                final var availableNames = namedArgs.keySet()
                    .stream()
                    .filter(k -> !excludedNames.contains(k));
                return Stream.concat(availableFlags, availableNames)
                    .collect(Collectors.toList());
            }

            final var result = entry.handler.getAutocompleteOptions(sender, $args);
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
            final var outputType = entry.handler.getOutputType();

            if (fieldType.isInterface()) {
                throw new RuntimeException(
                    String.format(
                        "Argument field \"%s\" on %s must be a concrete type, not an interface.",
                        field.getName(),
                        argumentsClass.getName()));
            }

            entry.isList = List.class.isAssignableFrom(fieldType);
            final var outputIsList = List.class.isAssignableFrom(outputType);

            if (!entry.isList && outputIsList) {
                throw new RuntimeException(
                    String.format(
                        "Handler output (%s) is not assignable to target field %s (%s) on %s.",
                        outputType,
                        field.getName(),
                        field.getType(),
                        argumentsClass.getName()));
            }

            if (!outputIsList) {
                Class<?> expectedOutput = getExpectedOutputClass(field, entry, fieldType);

                if (!expectedOutput.isAssignableFrom(entry.handler.getOutputType())) {
                    throw new RuntimeException(
                        String.format(
                            "Handler output (%s) is not assignable to target field %s (%s) on %s",
                            entry.handler.getOutputType(),
                            field.getName(),
                            expectedOutput,
                            argumentsClass.getName()));
                }
            }

            if (entry.isList) {
                entry.fieldSetter = (arguments, val) -> {
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
                        if (val instanceof List<?>listVal) {
                            list.addAll(listVal);
                        } else {
                            list.add(val);
                        }
                    } catch (IllegalAccessException e) {
                        LOG.error(e);
                    }
                };
            } else {
                entry.fieldSetter = (arguments, val) -> {
                    try {
                        field.set(arguments, val);
                    } catch (IllegalAccessException e) {
                        LOG.error(e);
                    }
                };
            }

        }
    }

    private Class<?> getExpectedOutputClass(Field field, ArgEntry entry, Class<?> fieldType) {
        Class<?> valueClass;

        if (entry.isList) {
            if (field.getGenericType() instanceof ParameterizedType parameterizedType
                && parameterizedType.getActualTypeArguments()[0] instanceof Class<?>typeInList) {
                valueClass = typeInList;
            } else {
                // From what I understand, this should only happen if the field type is a subclass of a generic
                // class. We would need to climb the type chain, somehow. Hopefully it never comes to that.
                throw new RuntimeException(
                    String.format(
                        "Could not get generic type from field \"%s\" on %s.",
                        field.getName(),
                        argumentsClass.getName()));
            }
        } else {
            valueClass = fieldType;
        }
        return valueClass;
    }

    private boolean evaluatePositionalArg(Field field, ArgEntry entry) {
        var posArg = field.getAnnotation(PositionalArg.class);
        if (posArg == null) {
            return false;
        }
        positionalArgs.put(posArg.index(), entry);
        entry.handler = argumentHandlers.get(posArg.handler());
        if (entry.handler == null) {
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
        entry.handler = argumentHandlers.get(flagArg.handler());
        if (entry.handler == null) {
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
        entry.handler = argumentHandlers.get(namedArg.handler());
        if (entry.handler == null) {
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

        public IArgumentHandler handler;
        public BiConsumer<Object, Object> fieldSetter;
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
