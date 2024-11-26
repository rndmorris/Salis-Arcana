package dev.rndmorris.tfixins.common.commands.arguments;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.AspectHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CommandNameHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CoordinateHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeModifierHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeTypeHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.QuantitativeAspectHandler;
import dev.rndmorris.tfixins.config.CommandSettings;

public class HelpArguments {
    private static ArgumentProcessor<HelpArguments> processor = null;

    public static ArgumentProcessor<HelpArguments> getProcessor() {
        if (processor == null) {
            processor = new ArgumentProcessor<>(
                HelpArguments.class,
                HelpArguments::new,
                new IArgumentHandler[] { CoordinateHandler.X.INSTANCE, CoordinateHandler.Y.INSTANCE,
                    CoordinateHandler.Z.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                    QuantitativeAspectHandler.INSTANCE, AspectHandler.INSTANCE });
        }
        return processor;
    }

    @PositionalArg(index = 0, parser = CommandNameHandler.class)
    public CommandSettings forCommand;

}
