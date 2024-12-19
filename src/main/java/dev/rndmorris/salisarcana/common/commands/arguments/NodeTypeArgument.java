package dev.rndmorris.salisarcana.common.commands.arguments;

import thaumcraft.api.nodes.NodeType;

public enum NodeTypeArgument {

    NORMAL(NodeType.NORMAL),
    UNSTABLE(NodeType.UNSTABLE),
    DARK(NodeType.DARK),
    TAINTED(NodeType.TAINTED),
    HUNGRY(NodeType.HUNGRY),
    PURE(NodeType.PURE);

    public final NodeType nodeType;

    NodeTypeArgument(NodeType nodeType) {
        this.nodeType = nodeType;
    }
}
