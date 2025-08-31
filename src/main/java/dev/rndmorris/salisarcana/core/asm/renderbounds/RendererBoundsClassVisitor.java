package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class RendererBoundsClassVisitor extends ClassVisitor {

    public RendererBoundsClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new RendererBoundsMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
    }
}
