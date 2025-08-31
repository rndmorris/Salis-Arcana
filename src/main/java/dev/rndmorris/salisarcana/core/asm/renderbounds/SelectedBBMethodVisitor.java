package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class SelectedBBMethodVisitor extends MethodVisitor {

    public SelectedBBMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }
}
