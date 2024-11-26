package dev.rndmorris.tfixins.common.commands.parsing;

import net.minecraft.command.ICommandSender;

import java.util.Iterator;

public class StringParser implements IArgType {
    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return current;
    }
}
