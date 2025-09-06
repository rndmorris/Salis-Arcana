package dev.rndmorris.salisarcana.core.asm.renderbounds;

import dev.rndmorris.salisarcana.lib.ObfuscationInfo;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class CollisionBoxesClassVisitor extends ClassVisitor {
    private final String addCollisionBoxesToList = ObfuscationInfo.ADD_COLLISION_BOXES_TO_LIST.getName();

    public CollisionBoxesClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(addCollisionBoxesToList)) {
            return new CollisionBoxesMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
