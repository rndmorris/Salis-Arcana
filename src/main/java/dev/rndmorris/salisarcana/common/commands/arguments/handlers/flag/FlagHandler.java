package dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import com.google.common.collect.PeekingIterator;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class FlagHandler implements IFlagArgumentHandler {

    public static final IArgumentHandler INSTANCE = new FlagHandler();

    @Override
    public Object parse(ICommandSender sender, PeekingIterator<String> input) {
        return true;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return boolean.class;
    }
}
