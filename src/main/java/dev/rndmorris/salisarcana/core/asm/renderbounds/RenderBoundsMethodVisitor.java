package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.objectweb.asm.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class RenderBoundsMethodVisitor extends MethodVisitor {
    public RenderBoundsMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    // Sequence:
    // INVOKEVIRTUAL net/minecraft/block/Block.setBlockBounds (FFFFFF)V
    // ALOAD #1 (RenderBlocks)
    // ALOAD #2 (Block)
    // INVOKEVIRTUAL net/minecraft/client/renderer/RenderBlocks.setRenderBoundsFromBlock (Lnet/minecraft/block/Block;)V


}
