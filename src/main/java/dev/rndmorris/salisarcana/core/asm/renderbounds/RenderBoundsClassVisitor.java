package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class RenderBoundsClassVisitor extends ClassVisitor {

    public RenderBoundsClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new RenderBoundsMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
    }
}
