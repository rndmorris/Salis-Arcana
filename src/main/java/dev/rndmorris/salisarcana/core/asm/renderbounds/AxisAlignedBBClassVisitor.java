package dev.rndmorris.salisarcana.core.asm.renderbounds;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

import dev.rndmorris.salisarcana.lib.ObfuscationInfo;

public class AxisAlignedBBClassVisitor extends ClassVisitor {

    public AxisAlignedBBClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
        String[] exceptions) {
        if (name.equals(ObfuscationInfo.ADD_COLLISION_BOXES_TO_LIST.getName())) {
            return new CollisionBBMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
        } else if (name.equals(ObfuscationInfo.GET_SELECTED_BOUNDING_BOX_FROM_POOL.getName())) {
            return new SelectedBBMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
        } else {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
