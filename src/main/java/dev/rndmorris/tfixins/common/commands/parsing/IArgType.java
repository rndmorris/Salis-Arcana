package dev.rndmorris.tfixins.common.commands.parsing;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.ICommandSender;

public interface IArgType {

    Object parse(ICommandSender sender, String current, Iterator<String> input);

    default List<String> getAutocompleteOptions(ICommandSender sender, String current, Iterator<String> args) {
        return null;
    };
}
