package dev.rndmorris.tfixins.common.commands.arguments;

import thaumcraft.api.nodes.NodeModifier;

public enum NodeModifierArgument {

    NONE(null),
    BRIGHT(NodeModifier.BRIGHT),
    PALE(NodeModifier.PALE),
    FADING(NodeModifier.FADING);

    public final NodeModifier modifier;

    NodeModifierArgument(NodeModifier modifier) {
        this.modifier = modifier;
    }
}
