package dev.rndmorris.tfixins.common.commands.parsing;

import net.minecraft.command.ICommandSender;

import java.util.Iterator;

public class FlagParser implements IArgType {

    public static final IArgType INSTANCE = new FlagParser();

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return true;
    }
}
