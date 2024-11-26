package dev.rndmorris.tfixins.common.commands;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.tfixins.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.positional.CommandNameHandler;
import dev.rndmorris.tfixins.config.CommandSettings;
import dev.rndmorris.tfixins.config.FixinsConfig;

public class HelpCommand extends FixinsCommandBase<HelpCommand.Arguments> {

    public HelpCommand() {
        super(FixinsConfig.commandsModule.help);
    }

    @Override
    protected void process(ICommandSender sender, String[] args) {
        final var arguments = argumentProcessor.process(sender, args);
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
    protected @Nonnull ArgumentProcessor<Arguments> initializeProcessor() {
        return new ArgumentProcessor<>(
            Arguments.class,
            Arguments::new,
            new IArgumentHandler[] { CommandNameHandler.INSTANCE });
    }

    public static class Arguments {

        @PositionalArg(index = 0, handler = CommandNameHandler.class, descLangKey = "cmd")
        public CommandSettings forCommand;

    }
}
