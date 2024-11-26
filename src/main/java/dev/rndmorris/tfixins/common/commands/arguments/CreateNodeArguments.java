package dev.rndmorris.tfixins.common.commands.arguments;

import java.util.ArrayList;
import java.util.List;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CoordinateHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.FlagHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeModifierHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeTypeHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.QuantitativeAspectHandler;

public class CreateNodeArguments {

    private static ArgumentProcessor<CreateNodeArguments> parser = null;

    public static ArgumentProcessor<CreateNodeArguments> getProcessor() {
        if (parser == null) {
            parser = new ArgumentProcessor<>(
                CreateNodeArguments.class,
                CreateNodeArguments::new,
                new IArgumentHandler[] { CoordinateHandler.X.INSTANCE, CoordinateHandler.Y.INSTANCE,
                    CoordinateHandler.Z.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                    FlagHandler.INSTANCE, QuantitativeAspectHandler.INSTANCE });
        }
        return parser;
    }

    @PositionalArg(index = 0, parser = CoordinateHandler.X.class)
    public int x;
    @PositionalArg(index = 1, parser = CoordinateHandler.Y.class)
    public int y;
    @PositionalArg(index = 2, parser = CoordinateHandler.Z.class)
    public int z;

    @NamedArg(name = "--silverwood", handler = FlagHandler.class, excludes = { "--eerie", "--small" })
    public boolean silverwood;

    @NamedArg(name = "--eerie", handler = FlagHandler.class, excludes = { "--silverwood" })
    public boolean eerie;

    @NamedArg(name = "--small", handler = FlagHandler.class, excludes = { "--silverwood", "-a" })
    public boolean small;

    @NamedArg(name = "-t", handler = NodeTypeHandler.class)
    public NodeTypeArgument nodeType;

    @NamedArg(name = "-m", handler = NodeModifierHandler.class)
    public NodeModifierArgument nodeModifier;

    @NamedArg(name = "-a", handler = QuantitativeAspectHandler.class, excludes = "--small")
    public List<QuantitativeAspectArgument> aspects = new ArrayList<>();

}
