package dev.rndmorris.tfixins.common.commands.arguments;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CommandNameHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.config.CommandSettings;

public class HelpArguments {
    private static ArgumentProcessor<HelpArguments> processor = null;

    public static ArgumentProcessor<HelpArguments> getProcessor() {
        if (processor == null) {
            processor = new ArgumentProcessor<>(
                HelpArguments.class,
                HelpArguments::new,
                new IArgumentHandler[] { CommandNameHandler.INSTANCE });
        }
        return processor;
    }

    @PositionalArg(index = 0, parser = CommandNameHandler.class)
    public CommandSettings forCommand;

}
