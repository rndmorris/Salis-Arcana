package dev.rndmorris.salisarcana.common.commands;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;

import dev.rndmorris.salisarcana.common.commands.arguments.ArgumentProcessor;
import dev.rndmorris.salisarcana.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.positional.CommandNameHandler;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.config.settings.CommandSettings;

public class HelpCommand extends ArcanaCommandBase<HelpCommand.Arguments> {

    public HelpCommand() {
        super(ConfigModuleRoot.commands.help);
    }

    @Override
    protected void process(ICommandSender sender, Arguments arguments, String[] args) {
        if (arguments.forCommand == null) {
            arguments.forCommand = this.settings;
        }

        final var command = arguments.forCommand.getCommand();
        if (command == null) {
            LOG.error("HelpCommand argument \"forCommand\" was not null, but a command was not found");
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

    public static class Arguments {

        @PositionalArg(index = 0, handler = CommandNameHandler.class, descLangKey = "cmd")
        public CommandSettings forCommand;

    }
}
