package dev.rndmorris.tfixins.common.commands.arguments;

import java.util.ArrayList;
import java.util.List;

import dev.rndmorris.tfixins.common.commands.arguments.annotations.NamedArg;
import dev.rndmorris.tfixins.common.commands.arguments.annotations.PositionalArg;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.AspectHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.CoordinateHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.IArgumentHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeModifierHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.NodeTypeHandler;
import dev.rndmorris.tfixins.common.commands.arguments.handlers.QuantitativeAspectHandler;
import thaumcraft.api.aspects.Aspect;

public class UpdateNodeArguments {

    private static ArgumentProcessor<UpdateNodeArguments> parser = null;

    public static ArgumentProcessor<UpdateNodeArguments> getProcessor() {
        if (parser == null) {
            parser = new ArgumentProcessor<>(
                UpdateNodeArguments.class,
                UpdateNodeArguments::new,
                new IArgumentHandler[] { CoordinateHandler.X.INSTANCE, CoordinateHandler.Y.INSTANCE,
                    CoordinateHandler.Z.INSTANCE, NodeTypeHandler.INSTANCE, NodeModifierHandler.INSTANCE,
                    QuantitativeAspectHandler.INSTANCE, AspectHandler.INSTANCE });
        }
        return parser;
    }

    @PositionalArg(index = 0, parser = CoordinateHandler.X.class)
    public int x;
    @PositionalArg(index = 1, parser = CoordinateHandler.Y.class)
    public int y;
    @PositionalArg(index = 2, parser = CoordinateHandler.Z.class)
    public int z;

    @NamedArg(name = "-t", handler = NodeTypeHandler.class)
    public NodeTypeArgument nodeType;

    @NamedArg(name = "-m", handler = NodeModifierHandler.class)
    public NodeModifierArgument nodeModifier;

    @NamedArg(name = "--set", handler = QuantitativeAspectHandler.class)
    public List<QuantitativeAspectArgument> addAspects = new ArrayList<>();

    @NamedArg(name = "--rem", handler = AspectHandler.class)
    public List<Aspect> removeAspects = new ArrayList<>();
}
