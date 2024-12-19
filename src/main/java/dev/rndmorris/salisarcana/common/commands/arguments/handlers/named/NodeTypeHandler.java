package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import dev.rndmorris.salisarcana.common.commands.arguments.NodeTypeArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class NodeTypeHandler extends EnumHandler<NodeTypeArgument> {

    public static final IArgumentHandler INSTANCE = new NodeTypeHandler();

    public NodeTypeHandler() {
        super(NodeTypeArgument.class);
    }

}
