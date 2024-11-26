package dev.rndmorris.tfixins.common.commands;

import dev.rndmorris.tfixins.common.commands.arguments.HelpArguments;
import dev.rndmorris.tfixins.config.FixinsConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import java.util.Arrays;
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
        return HelpArguments.getProcessor().getAutocompletionSuggestions(sender, args);
    }

    @Override
    public void printHelp(ICommandSender sender) {
        Arrays.stream(new String[] {
            "tfixins:command.help.desc",
            "tfixins:command.usage",
            "tfixins:command.help.usage",
            "tfixins:command.args",
            "tfixins:command.help.args.01",
        }).map(ChatComponentTranslation::new).forEachOrdered(sender::addChatMessage);
    }
}
