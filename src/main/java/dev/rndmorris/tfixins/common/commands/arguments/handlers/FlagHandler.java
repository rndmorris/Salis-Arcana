package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import java.util.Iterator;

import net.minecraft.command.ICommandSender;

public class FlagHandler implements IArgumentHandler {

    public static final IArgumentHandler INSTANCE = new FlagHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return true;
    }
}
