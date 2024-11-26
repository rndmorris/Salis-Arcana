package dev.rndmorris.tfixins.common.commands.arguments.handlers;

import dev.rndmorris.tfixins.common.commands.arguments.NodeModifierArgument;

public class NodeModifierHandler extends EnumHandler<NodeModifierArgument> {

    public static final IArgumentHandler INSTANCE = new NodeModifierHandler();

    public NodeModifierHandler() {
        super(NodeModifierArgument.class);
    }

}
