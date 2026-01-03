package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.tree.ClassNode;

public interface ClassNodeTransformer extends ClassTransformer {

    void transformNode(ClassNode node);

    int apiLevel();

    @Override
    default ClassVisitor transform(ClassVisitor output) {
        return new ClassNodeAdapter(this, output);
    }

    @Override
    default int getFlags() {
        // TODO: Figure out if ClassNodes will produce frames automatically, or if COMPUTE_FRAMES is necessary too.
        return ClassWriter.COMPUTE_MAXS;
    }
}
