package dev.rndmorris.salisarcana.core.asm;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

import java.util.Set;

public class MethodRemoverClassVisitor extends ClassVisitor {
    public static IClassVisitorFactory factory(Set<String> toRemove) {
        return output -> new MethodRemoverClassVisitor(output, toRemove);
    }

    private final Set<String> toRemove;

    protected MethodRemoverClassVisitor(ClassVisitor classVisitor, Set<String> toRemove) {
        super(Opcodes.ASM9, classVisitor);
        this.toRemove = toRemove;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return this.toRemove.contains(name) ? null : super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
