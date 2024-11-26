package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import dev.rndmorris.tfixins.common.commands.arguments.NodeTypeArgument;

public class NodeTypeHandler extends EnumHandler<NodeTypeArgument> {

    public static final IArgumentHandler INSTANCE = new NodeTypeHandler();

    public NodeTypeHandler() {
        super(NodeTypeArgument.class);
    }

}
