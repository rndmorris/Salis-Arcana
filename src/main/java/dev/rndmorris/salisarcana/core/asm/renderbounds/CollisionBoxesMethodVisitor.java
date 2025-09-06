package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.*;

public class CollisionBoxesMethodVisitor extends MethodVisitor {
    private final Label first = new Label();

    private boolean lastFrameHasBoundingBox = false;

    public CollisionBoxesMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    // spotless:off
    // Variables 0-7 are the arguments to the method
    // [this], World worldIn, int x, int y, int z, AxisAlignedBB mask, List<net.minecraft.util.AxisAlignedBB> list, Entity collider
    // Variable 8 will be the new AxisAlignedBB to add to the output.
    // Variable 9+ are the locals, remapped +1
    // spotless:on

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        super.visitVarInsn(opcode, varIndex <= 7 ? varIndex : varIndex + 1);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index <= 7 ? index : index + 1);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        int[] newIndices = new int[index.length];

        for (int i = 0; i < index.length; i++) {
            int idx = index[i];
            newIndices[i] = idx <= 7 ? idx : idx + 1;
        }

        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, newIndices, descriptor, visible);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        // TODO Fix this.
        switch (type) {
            case Opcodes.F_SAME:

                break;
        }
        this.lastFrameHasBoundingBox = true;
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals + 1);
    }

    // Add an entry for our new local to the LVT (just for fun)
    @Override
    public void visitCode() {
        super.visitCode();
        super.visitLabel(this.first);
    }

    @Override
    public void visitEnd() {
        final Label last = new Label();
        super.visitLabel(last);
        super.visitLocalVariable(
            "salisArcana$newBoundingBoxToAdd",
            "Lnet/minecraft/util/AxisAlignedBB;",
            null,
            this.first,
            last,
            8);
        super.visitEnd();
    }

    // The actual modifications:
    // When [this];setBlockBounds(FFFFFF)V is called, instead
    //  - Load x, y, z
    //  - Call a hook to create the new AxisAlignedBB
    //  - Save AABB to var 8
    //  - Pop the [this]
    // When [super];addCollisionBoxesToList()V is called
    //  -
}
