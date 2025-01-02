package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.tree.MethodNode;

public interface IAsmEditor {

    void edit(MethodNode method);

    String getClassName();

    String getMethodName();

    String getMethodDesc();
}
