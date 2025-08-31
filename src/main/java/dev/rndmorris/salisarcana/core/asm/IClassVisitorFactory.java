package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;

@FunctionalInterface
public interface IClassVisitorFactory {

    ClassVisitor transform(ClassVisitor output);
}
