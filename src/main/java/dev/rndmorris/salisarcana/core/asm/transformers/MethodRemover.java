package dev.rndmorris.salisarcana.core.asm.transformers;

import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

import dev.rndmorris.salisarcana.core.SalisArcanaCore;
import dev.rndmorris.salisarcana.core.asm.ClassTransformer;

public class MethodRemover implements ClassTransformer {

    private final String obfName;
    private final String deobfName;

    public MethodRemover(String obfName, String deobfName) {
        this.obfName = obfName;
        this.deobfName = deobfName;
    }

    @Override
    public ClassVisitor transform(ClassVisitor output) {
        return new Visitor(SalisArcanaCore.isObf() ? obfName : deobfName, output);
    }

    private static class Visitor extends ClassVisitor {

        private final String method;

        private Visitor(String method, ClassVisitor output) {
            super(Opcodes.ASM9, output);
            this.method = method;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
            if (method.equals(name)) return null;
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
