package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class RenderBoundsMethodVisitor extends MethodVisitor {
    // Sequence:
    // 1) INVOKEVIRTUAL net/minecraft/block/Block.setBlockBounds(FFFFFF)V
    // 2) ALOAD #1 (RenderBlocks)
    // 3) ALOAD #2 (Block)
    // 4) INVOKEVIRTUAL
    // net/minecraft/client/renderer/RenderBlocks.setRenderBoundsFromBlock(Lnet/minecraft/block/Block;)V

    private int sequenceStep = 0;
    private int aload1 = 0;
    private int aload2 = 0;

    public RenderBoundsMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        if (opcode == Opcodes.ALOAD) {
            if (sequenceStep == 1) {
                aload1 = varIndex;
                sequenceStep = 2;
                return;
            } else if (sequenceStep == 2) {
                aload2 = varIndex;
                sequenceStep = 3;
                return;
            }
        }

        wrongSequence();
        super.visitVarInsn(opcode, varIndex);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (sequenceStep == 0) {
            if (opcode == Opcodes.INVOKEVIRTUAL && "net/minecraft/block/Block".equals(owner)
                && "setBlockBounds".equals(name)
                && "(FFFFFF)V".equals(descriptor)) {
                sequenceStep = 1;
                return;
            }
        } else if (sequenceStep == 3) {
            if (opcode == Opcodes.INVOKEVIRTUAL && "net/minecraft/client/renderer/RenderBlocks".equals(owner)
                && "setRenderBoundsFromBlock".equals(name)
                && "(Lnet/minecraft/block/Block;)V".equals(descriptor)) {
                injectNewSequence();
                return;
            }
        }

        wrongSequence();
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private void wrongSequence() {
        if (sequenceStep == 0) return;

        seq: {
            if (sequenceStep >= 1) {
                super.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "net/minecraft/block/Block",
                    "setBlockBounds",
                    "(FFFFFF)V",
                    false);
            } else break seq;

            if (sequenceStep >= 2) {
                super.visitVarInsn(Opcodes.ALOAD, aload1);
            } else break seq;

            if (sequenceStep >= 3) {
                super.visitVarInsn(Opcodes.ALOAD, aload2);
            }
        }

        sequenceStep = 0;
    }

    private void injectNewSequence() {
        // We know the stack currently ends with Lnet/minecraft/block/Block;FFFFFF since the first step of the sequence
        // is Block.setBlockBounds
        // We know aload1 is RenderBlocks since it was the first argument on the stack to
        // RenderBlocks.setRenderBoundsFromBlock.

        super.visitVarInsn(Opcodes.ALOAD, aload1);
        super.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "dev/rndmorris/salisarcana/core/SalisArcanaClientHooks",
            "setRenderBoundsWithoutBlock",
            "(Lnet/minecraft/block/Block;FFFFFFLnet/minecraft/client/renderer/RenderBlocks;)V",
            false);

        sequenceStep = 0;
    }

    // Non-matching opcodes
    @Override
    public void visitInsn(int opcode) {
        wrongSequence();
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        wrongSequence();
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitLdcInsn(Object value) {
        wrongSequence();
        super.visitLdcInsn(value);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
        Object... bootstrapMethodArguments) {
        wrongSequence();
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        wrongSequence();
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        wrongSequence();
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        wrongSequence();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        wrongSequence();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitIincInsn(int varIndex, int increment) {
        wrongSequence();
        super.visitIincInsn(varIndex, increment);
    }
}
