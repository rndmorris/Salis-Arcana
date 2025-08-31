package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class CollisionBBMethodVisitor extends MethodVisitor {

    public CollisionBBMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }
}
