package dev.rndmorris.tfixins.common.commands.parsing;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Iterator;

public class BooleanParser implements IArgType {
    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return CommandBase.parseBoolean(sender, current);
    }
}
