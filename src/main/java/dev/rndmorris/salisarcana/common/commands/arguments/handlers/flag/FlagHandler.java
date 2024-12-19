package dev.rndmorris.salisarcana.common.commands.arguments.handlers.flag;

import java.util.Iterator;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class FlagHandler implements IFlagArgumentHandler {

    public static final IArgumentHandler INSTANCE = new FlagHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return true;
    }

    @Nonnull
    @Override
    public Class<?> getOutputType() {
        return boolean.class;
    }
}
