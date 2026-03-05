package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.tree.ClassNode;

public final class ClassNodeAdapter extends ClassNode {
    // TODO: If multiple Class Node transformers need to be applied to the same class, figure out a way
    // TODO: to avoid rebuilding the class node for each transform.

    private final ClassNodeTransformer transformer;
    private final ClassVisitor output;

    public ClassNodeAdapter(ClassNodeTransformer transformer, ClassVisitor output) {
        super(transformer.apiLevel());
        this.transformer = transformer;
        this.output = output;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        transformer.transformNode(this);
        super.accept(this.output);
    }
}
