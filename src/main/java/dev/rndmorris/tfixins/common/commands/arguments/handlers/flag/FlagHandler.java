package dev.rndmorris.tfixins.common.commands.arguments.handlers.flag;

import java.util.Iterator;

import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import net.minecraft.command.ICommandSender;

public class FlagHandler implements IFlagArgumentHandler {

    public static final IArgumentHandler INSTANCE = new FlagHandler();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return true;
    }
}
