package dev.rndmorris.salisarcana.core.asm.compat;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;

public class TMixinsCompat extends ModCompatEditor {

    public TMixinsCompat(String className, String methodName, String methodDesc) {
        super(className, methodName, methodDesc);
    }

    @Override
    public void edit(MethodNode method) {
        InsnNode node = null;
        for (int i = 0; i < method.instructions.size(); i++) {
            if (method.instructions.get(i)
                .getOpcode() == Opcodes.ARETURN) {
                node = (InsnNode) method.instructions.get(i);
                break;

            }
        }
        MethodInsnNode methodNode = new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "dev/rndmorris/salisarcana/core/asm/compat/TMixinsCompat",
            "apply_compat",
            "(Ljava/util/List;)Ljava/util/List;",
            false);
        method.instructions.insertBefore(node, methodNode);
    }
}
