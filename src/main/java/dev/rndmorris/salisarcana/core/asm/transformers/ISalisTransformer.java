package dev.rndmorris.salisarcana.core.asm.transformers;

import org.objectweb.asm.tree.ClassNode;

public interface ISalisTransformer {

    String getTargetClassName();

    void transform(ClassNode classNode);

}
