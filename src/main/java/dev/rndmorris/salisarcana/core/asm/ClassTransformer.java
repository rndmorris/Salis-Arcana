package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;

@FunctionalInterface
public interface ClassTransformer {

    ClassVisitor transform(ClassVisitor output);

    default int getFlags() {
        return 0;
    }

}
