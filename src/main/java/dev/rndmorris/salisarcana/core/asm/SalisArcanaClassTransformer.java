package dev.rndmorris.salisarcana.core.asm;

import java.util.ArrayList;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.core.asm.transformers.ISalisTransformer;
import dev.rndmorris.salisarcana.core.asm.transformers.ModCompatEditor;

public class SalisArcanaClassTransformer implements IClassTransformer {

    // if there is more target classes this can be changed later to a HashMap<String, List<ISalisTransformer>>
    // see
    // https://github.com/Alexdoru/MWE/blob/master/src/main/java/fr/alexdoru/mwe/asm/transformers/MWEClassTransformer.java
    private final ArrayList<ISalisTransformer> transformers = new ArrayList<>();

    public SalisArcanaClassTransformer() {
        transformers.add(
            new ModCompatEditor(
                "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins",
                "getMixins",
                "(Ljava/util/Set;)Ljava/util/List;")
                    .addConflict("MixinBlockCosmeticSolid", SalisConfig.bugfixes.beaconBlockFixSetting)
                    .addConflict("MixinBlockCandleRenderer", SalisConfig.bugfixes.candleRendererCrashes)
                    .addConflict("MixinBlockCandle", SalisConfig.bugfixes.candleRendererCrashes)
                    .addConflict("MixinItemShard", SalisConfig.bugfixes.itemShardColor)
                    .addConflict("MixinWandManager", SalisConfig.features.useAllBaublesSlots)
                    .addConflict("MixinEventHandlerRunic", SalisConfig.features.useAllBaublesSlots)
                    .addConflict("MixinWarpEvents_BaubleSlots", SalisConfig.features.useAllBaublesSlots));
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;
        for (int i = 0; i < transformers.size(); i++) {
            ISalisTransformer editor = transformers.get(i);
            if (transformedName.equals(editor.getTargetClassName())) {
                bytes = transformBytes(bytes, editor);
            }
        }
        return bytes;
    }

    private static byte[] transformBytes(byte[] bytes, ISalisTransformer editor) {
        ClassReader reader = new ClassReader(bytes);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        editor.transform(node);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
