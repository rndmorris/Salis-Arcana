package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;

public final class ChainedTransformer implements ClassTransformer {

    private final ClassTransformer first;
    private final ClassTransformer second;

    public ChainedTransformer(ClassTransformer first, ClassTransformer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ClassVisitor transform(ClassVisitor output) {
        return second.transform(first.transform(output));
    }

    @Override
    public int getFlags() {
        return first.getFlags() | second.getFlags();
    }
}
