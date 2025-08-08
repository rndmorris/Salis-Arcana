package dev.rndmorris.salisarcana.core;

import static org.spongepowered.asm.lib.Opcodes.ASM5;

import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodNode;

import dev.rndmorris.salisarcana.core.asm.IAsmEditor;

public class SalisArcanaClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        if (bytes == null) return null;

        for (IAsmEditor editor : SalisArcanaCore.editors) {
            if (s1.equals(editor.getClassName())) {
                bytes = updateMethod(bytes, editor);
            }
        }

        return bytes;
    }

    private byte[] updateMethod(byte[] bytes, IAsmEditor editor) {
        ClassReader reader = new ClassReader(bytes);
        ClassNode node = new ClassNode(ASM5);
        reader.accept(node, 0);

        boolean foundMethod = false;
        for (MethodNode method : node.methods) {
            if (method.name.equals(editor.getMethodName()) && method.desc.equals(editor.getMethodDesc())) {
                foundMethod = true;
                editor.edit(method);
            }
        }

        if (!foundMethod) throw new RuntimeException(
                "SalisArcana failed to find a method named " + editor.getMethodName()
                        + " in class "
                        + editor.getClassName());

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
