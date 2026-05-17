package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.tree.ClassNode;

public interface ClassNodeTransformer extends ClassTransformer {

    void transformNode(ClassNode node);

    int apiLevel();

    @Override
    default ClassVisitor transform(ClassVisitor output) {
        return new ClassNodeAdapter(this, output);
    }

}
