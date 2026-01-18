package dev.rndmorris.salisarcana.core.asm.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import dev.rndmorris.salisarcana.config.IEnabler;

/**
 * This class is used to inject compatibility checks into the mixin loading process
 * <p>
 * Specifically, it is primarily used to disable non-configurable incompatible mixins in other mods.
 * </p>
 * The method anything extending or using this class should target should be the
 * {@code getMixins(List<String> loadedCoreMods)}
 * method in the LateMixin class.
 * <p>
 * You can use this for other methods as well, but you will have to extend this class and override
 * {@code transformClassnode(MethodNode method)}
 * to target the correct method
 */
public class ModCompatEditor implements ISalisTransformer {

    // Static list to fetch any given transformer from the editor
    private static final List<ModCompatEditor> transformers = new ArrayList<>();
    // compatibility map for what mixin to disable based on what setting
    private final HashMap<String, IEnabler> compatMap = new HashMap<>();
    private final String className, methodName, methodDesc;

    public ModCompatEditor(String className, String methodName, String methodDesc) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        transformers.add(this);
    }

    @Override
    public String getTargetClassName() {
        return className;
    }

    /**
     * Injects the compatibility check into the method.
     * This is a default editor that modifies the {@code return mixins} statement to
     * {@code ModCompatEditor.getTransformer(className, methodName, methodDesc).apply_compat(mixins);}
     * <p>
     * Of course, this won't work in every case, but it's a good generic solution
     * <p>
     * We make the assumption that the method will only have one ARETURN opcode, so any method with multiple return
     * statements will break
     */
    @Override
    public void transform(ClassNode classNode) {

        MethodNode method = null;
        for (MethodNode mn : classNode.methods) {
            if (mn.name.equals(methodName) && mn.desc.equals(methodDesc)) {
                method = mn;
            }
        }

        if (method == null) {
            throw new RuntimeException(
                "SalisArcana failed to find a method named " + methodName + " in class " + className);
        }

        VarInsnNode previousNode = null;
        InsnNode node = null;
        for (int i = 0; i < method.instructions.size(); i++) {
            if (method.instructions.get(i)
                .getOpcode() == Opcodes.ARETURN) {
                // We've found the return statement
                node = (InsnNode) method.instructions.get(i);
                if (method.instructions.get(i - 1) instanceof VarInsnNode) {
                    // Previous node should be an ALOAD # statement
                    previousNode = (VarInsnNode) method.instructions.get(i - 1);
                }
                break;

            }
        }
        if (node == null || previousNode == null || previousNode.getOpcode() != Opcodes.ALOAD) {
            throw new RuntimeException(
                "SalisArcana failed to find an injection point in method " + methodName + " in class " + className);
        }
        int index = previousNode.var; // The index of the local variable that holds the List<String>

        // We need to load the class name, method name, and method desc onto the stack
        LdcInsnNode loadClassName = new LdcInsnNode(this.className);
        LdcInsnNode loadMethodName = new LdcInsnNode(this.methodName);
        LdcInsnNode loadMethodDesc = new LdcInsnNode(this.methodDesc);

        // We need to call the static method ModCompatEditor.getTransformer, popping the strings off the stack
        MethodInsnNode getTransformer = new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "dev/rndmorris/salisarcana/core/asm/transformers/ModCompatEditor",
            "getTransformer",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ldev/rndmorris/salisarcana/core/asm/transformers/ModCompatEditor;",
            false);

        // We need to re-load the List<String> mixins onto the stack
        VarInsnNode load = new VarInsnNode(Opcodes.ALOAD, index);
        // We need to call the instance method ModCompatEditor.apply_compat, popping the List<String> off the stack
        MethodInsnNode methodNode = new MethodInsnNode(
            Opcodes.INVOKEVIRTUAL,
            "dev/rndmorris/salisarcana/core/asm/transformers/ModCompatEditor",
            "apply_compat",
            "(Ljava/util/List;)Ljava/util/List;",
            false);

        InsnList list = new InsnList();
        list.add(loadClassName);
        list.add(loadMethodName);
        list.add(loadMethodDesc);
        list.add(getTransformer);
        list.add(load);
        list.add(methodNode);
        method.instructions.insertBefore(node, list);

    }

    /**
     * Adds a mixin to the compatibility map
     *
     * @param mixin   The mixin to add
     * @param enabler The enabler to check if the mixin should be disabled
     * @return The ModCompatEditor instance
     */
    public ModCompatEditor addConflict(String mixin, IEnabler enabler) {
        compatMap.put(mixin, enabler);
        return this;
    }

    /* ============ ASM Hook ============ */

    @SuppressWarnings("unused")
    public List<String> apply_compat(List<String> mixins) {
        for (String mixin : new ArrayList<>(mixins)) {
            if (compatMap.containsKey(mixin)) {
                if (compatMap.get(mixin)
                    .isEnabled()) {
                    mixins.remove(mixin);
                }
            }
        }
        return mixins;
    }

    /**
     * Fetches a transformer from the static list
     *
     * @param className  The class name
     * @param methodName The method name
     * @param methodDesc The method desc
     * @return The ModCompatEditor instance
     */
    @SuppressWarnings("unused")
    public static ModCompatEditor getTransformer(String className, String methodName, String methodDesc) {
        for (ModCompatEditor editor : transformers) {
            if (editor.className.equals(className) && editor.methodName.equals(methodName)
                && editor.methodDesc.equals(methodDesc)) {
                return editor;
            }
        }
        return null;
    }
}
