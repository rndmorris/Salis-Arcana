package dev.rndmorris.tfixins.common.commands;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.config.FixinsConfig;

import java.util.List;

public class CreateNodeCommand extends FixinsCommandBase {

    public CreateNodeCommand() {
        super(FixinsConfig.commandsModule.createNode);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return super.addTabCompletionOptions(sender, args);
    }

    private static class CommandFlags {

    }
}
