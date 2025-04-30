package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.CommandNameHandler;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class HelpCommand extends ArcanaCommandBase<HelpCommand.Arguments> {

    public HelpCommand() {
        super(SalisConfig.commands.help);
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.forCommand == null) {
            arguments.forCommand = settings;
        }

        final var command = arguments.forCommand.getCommand();
        if (command == null) {
            LOG.error("HelpCommand argument \"forCommand\" was not null, but a command was not found. How?!?");
            return;
        }

        command.printHelp(sender);
    }

    @Override
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CommandNameHandler.INSTANCE });
    }

    @Override
    protected int minimumRequiredArgs() {
        return 0;
    }

    @Override
    public void printHelp(ICommandSender sender) {
        super.printHelp(sender);
        printAvailableCommands(sender);
    }

    private void printAvailableCommands(ICommandSender sender) {
        final var listMessage = new ChatComponentText("");
        var any = false;

        final var settings$ = SalisConfig.commands.getCommandsSettings()
            .iterator();

        while (settings$.hasNext()) {
            final var setting = settings$.next();

            if (!setting.isEnabled()) {
                continue;
            }
            if (!sender.canCommandSenderUseCommand(setting.getPermissionLevel(), settings.getFullName())) {
                continue;
            }
            any = true;
            listMessage.appendText(setting.getFullName());
            if (settings$.hasNext()) {
                listMessage.appendText(", ");
            }
        }

        if (any) {
            sender.addChatMessage(
                new ChatComponentTranslation("salisarcana:command.help.available").setChatStyle(titleStyle()));
            sender.addChatMessage(listMessage);
            return;
        }
        // this should *never* happen, since the user needs to be able to run "salisarcana-help" at all
        sender.addChatMessage(new ChatComponentTranslation("salisarcana:command.help.nocommands"));
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CommandNameHandler.class, descLangKey = "cmd")
        public CommandSettings forCommand;

    }
}
