package dev.rndmorris.tfixins.common.commands;

import dev.rndmorris.tfixins.common.commands.arguments.HelpArguments;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class HelpCommand extends FixinsCommandBase {
    public HelpCommand() {
        super(FixinsConfig.commandsModule.help);
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var arguments = HelpArguments.getProcessor().process(sender, args);
        if (arguments.forCommand == null) {
            return;
        }

        final var command = arguments.forCommand.getCommand();
        if (command == null) {
            return;
        }

        command.printHelp(sender);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return super.addTabCompletionOptions(sender, args);
    }
}
