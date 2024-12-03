package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.named.INamedArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.positional.IPositionalArgumentHandler;

public class IntHandler implements INamedArgumentHandler, IPositionalArgumentHandler {

    public static final IntHandler DEFAULT_INSTANCE = new IntHandler();

    private final int minValue;
    private final int maxValue;
    private final int suggestedValue;

    public IntHandler() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    public IntHandler(int minValue, int maxValue, int suggestedValue) {

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.suggestedValue = suggestedValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getSuggestedValue() {
        return suggestedValue;
    }

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> args) {
        return CommandBase.parseIntBounded(sender, current, minValue, maxValue);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        if (args.hasNext()) {
            return null;
        }
        return Collections.singletonList(Integer.toString(suggestedValue));
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return int.class;
    }
}
