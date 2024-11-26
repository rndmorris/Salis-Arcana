package dev.rndmorris.tfixins.common.commands.parsing;

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

import dev.rndmorris.tfixins.lib.ClassComparator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;

public class ArgParser<TArguments> {

    private final Map<Class<? extends IArgType>, IArgType> argTypes = new TreeMap<>(new ClassComparator());
    private final Class<TArguments> argumentsClass;
    private final Supplier<TArguments> initializer;

    private final Map<Integer, ArgEntry> positionalArgs = new TreeMap<>();
    private final Map<String, ArgEntry> namedArgs = new TreeMap<>();

    public ArgParser(Class<TArguments> argumentsClass, Supplier<TArguments> initializer, IArgType... argTypes) {
        this.argumentsClass = argumentsClass;
        this.initializer = initializer;
        for (var type : argTypes) {
            this.argTypes.put(type.getClass(), type);
        }
        readArgumentsObject();
    }

    public TArguments parse(ICommandSender sender, String[] args) {

        final var foundNames = new TreeSet<>(String::compareToIgnoreCase);
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
                if (!namedEntry.isList || !foundNames.contains(current)) {
                    foundNames.add(current);
                    entry = namedEntry;
                }
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

    public List<String> getTabOptions(ICommandSender sender, String[] args) {
        final var foundNames = new TreeSet<String>();

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
                if (!namedEntry.isList || !foundNames.contains(current)) {
                    foundNames.add(current);
                    entry = namedEntry;
                }
            }

            if (entry == null) {
                return namedArgs.keySet().stream().filter(k -> !foundNames.contains(k)).collect(Collectors.toList());
            }

            final var result = entry.parser.getAutocompleteOptions(sender, current, $args);
            if (result != null) {
                return result;
            }

            index += 1;
        }

        return Collections.emptyList();
    }

    private void readArgumentsObject() {
        for (var field : argumentsClass.getFields()) {
            field.setAccessible(true);

            final var entry = new ArgEntry();

            PositionalArg posArg;
            NamedArg namedArg = null;
            if ((posArg = field.getAnnotation(PositionalArg.class)) != null) {
                positionalArgs.put(posArg.index(), entry);
                entry.parser = argTypes.get(posArg.parser());
            }
            else if ((namedArg = field.getAnnotation(NamedArg.class)) != null) {
                namedArgs.put(namedArg.name(), entry);
                entry.parser = argTypes.get(namedArg.parser());
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
                        }
                        else {
                            //noinspection unchecked
                            list = (List<Object>) fieldObj;
                        }

                        list.add(val);
                    } catch (IllegalAccessException e)
                    {
                        LOG.error(e);
                    }
                };
            }
            else {
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

        public IArgType parser;
        public BiConsumer<Object, Object> setter;
        public boolean isList;
    }
}
