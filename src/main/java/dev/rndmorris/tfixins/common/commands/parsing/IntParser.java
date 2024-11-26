package dev.rndmorris.tfixins.common.commands.parsing;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IntParser implements IArgType {

    @Override
    public Object parse(ICommandSender sender, String current, Iterator<String> input) {
        return CommandBase.parseInt(sender, current);
    }

    @Override
    public List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        return Collections.singletonList("0");
    }
}
