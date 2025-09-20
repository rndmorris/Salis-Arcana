package dev.rndmorris.salisarcana.core.asm.renderbounds;

import dev.rndmorris.salisarcana.lib.ObfuscationInfo;
import org.spongepowered.asm.lib.*;

import java.util.Locale;

public class CollisionBoxesMethodVisitor extends MethodVisitor {
    private final String setBlockBounds = ObfuscationInfo.SET_BLOCK_BOUNDS.getName();
    private final String addCollisionBoxesToList = ObfuscationInfo.ADD_COLLISION_BOXES_TO_LIST.getName();

    private final Label first = new Label();

    private boolean isFirstFrame = true;

    private final String className;

    public CollisionBoxesMethodVisitor(MethodVisitor mv, String className) {
        super(Opcodes.ASM9, mv);
        this.className = className;
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
    public void visitIincInsn(int varIndex, int increment) {
        super.visitIincInsn(varIndex <= 7 ? varIndex : varIndex + 1, increment);
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
        if (type == Opcodes.F_FULL) {
            // F_FULL is must be handled every time
            final var newLocal = new Object[local.length + 1];
            System.arraycopy(local, 0, newLocal, 0, 8);
            newLocal[8] = "net/minecraft/util/AxisAlignedBB"; // salisArcana$newBoundingBoxToAdd
            System.arraycopy(local, 8, newLocal, 9, local.length - 8);
            local = newLocal;
            numLocal++;
        } else if (isFirstFrame) {
            // We know that the previous stack map frame is the beginning of the method - only arguments, zero locals.
            // Also, no valid Java code will produce F_CHOP as the first frame.

            switch (type) {
                case Opcodes.F_SAME:
                    type = Opcodes.F_APPEND;
                    numLocal = 1;
                    local = new Object[] { "net/minecraft/util/AxisAlignedBB" }; // salisArcana$newBoundingBoxToAdd
                    break;
                case Opcodes.F_APPEND:
                    if (numLocal < 3) {
                        // We can append up to 3 locals, so we can add an extra local.
                        final var newLocal = new Object[local.length + 1];
                        newLocal[0] = "net/minecraft/util/AxisAlignedBB"; // salisArcana$newBoundingBoxToAdd
                        System.arraycopy(local, 0, newLocal, 1, local.length);
                        local = newLocal;
                        numLocal++;
                        break;
                    }
                    // Otherwise, we need a full frame
                case Opcodes.F_SAME1: // We need a full frame
                    type = Opcodes.F_FULL;
                    final var args = new Object[] {
                        this.className,                     // this
                        "net/minecraft/world/World",        // worldIn
                        Opcodes.INTEGER,                    // x
                        Opcodes.INTEGER,                    // y
                        Opcodes.INTEGER,                    // z
                        "net/minecraft/util/AxisAlignedBB", // mask
                        "java/util/List",                   // list
                        "net/minecraft/entity/Entity",      // collider
                        "net/minecraft/util/AxisAlignedBB", // salisArcana$newBoundingBoxToAdd
                    };

                    if (numLocal > 0) {
                        final var newLocal = new Object[9 + local.length];
                        System.arraycopy(args, 0, newLocal, 0, 9);
                        System.arraycopy(local, 0, newLocal, 9, local.length);

                        local = newLocal;
                        numLocal += 9;
                    } else {
                        local = args;
                        numLocal = 9;
                    }
                    break;
            }
        }

        isFirstFrame = false;

        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    // Add an extra local slot for our new local.
    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals + 1);
    }


    @Override
    public void visitCode() {
        super.visitCode();

        // For the LVT
        super.visitLabel(this.first);

        // Set our variable to null, in case we call super without doing anything
        super.visitInsn(Opcodes.ACONST_NULL);
        super.visitVarInsn(Opcodes.ASTORE, 8);
    }

    // Add an entry for our new local to the LVT (just for fun)
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
    //  - Save the AxisAlignedBB to var 8
    //  - Pop the [this]
    // When [super];addCollisionBoxesToList(...)V is called
    //  - Load var 8
    //  - Call the other hook to add it to the list

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (name.equals(this.setBlockBounds) && descriptor.equals("(FFFFFF)V")) {
            super.visitVarInsn(Opcodes.ILOAD, 2);
            super.visitVarInsn(Opcodes.ILOAD, 3);
            super.visitVarInsn(Opcodes.ILOAD, 4);
            super.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "dev/rndmorris/salisarcana/core/SalisArcanaHooks",
                "createBoundingBox",
                "(FFFFFFIII)Lnet/minecraft/util/AxisAlignedBB;",
                false);
            super.visitVarInsn(Opcodes.ASTORE, 8);
            super.visitInsn(Opcodes.POP);
        } else if (name.equals(this.addCollisionBoxesToList)
            && descriptor.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V")) {
            super.visitVarInsn(Opcodes.ALOAD, 8);
            super.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "dev/rndmorris/salisarcana/core/SalisArcanaHooks",
                "addBoundingBox",
                "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;IIILnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V",
                false);
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
