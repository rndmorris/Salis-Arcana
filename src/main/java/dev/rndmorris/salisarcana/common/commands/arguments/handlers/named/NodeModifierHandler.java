package dev.rndmorris.salisarcana.common.commands.arguments.handlers.named;

import dev.rndmorris.salisarcana.common.commands.arguments.NodeModifierArgument;
import dev.rndmorris.salisarcana.common.commands.arguments.handlers.IArgumentHandler;

public class NodeModifierHandler extends EnumHandler<NodeModifierArgument> {

    public static final IArgumentHandler INSTANCE = new NodeModifierHandler();

    public NodeModifierHandler() {
        super(NodeModifierArgument.class);
    }

}
