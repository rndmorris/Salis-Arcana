package dev.rndmorris.salisarcana.core.asm;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.core.asm.transformers.MethodRemover;
import dev.rndmorris.salisarcana.core.asm.transformers.ModCompatEditor;

public class SalisArcanaClassTransformer implements IClassTransformer {

    private final HashMap<String, ClassTransformer> transformers = new HashMap<>();

    public SalisArcanaClassTransformer() {
        final var thaumicMixinsCompat = new ModCompatEditor(
            "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins",
            "getMixins",
            "(Ljava/util/Set;)Ljava/util/List;")
                .addConflict("MixinBlockCosmeticSolid", SalisConfig.bugfixes.beaconBlockFixSetting)
                .addConflict("MixinBlockCandleRenderer", SalisConfig.bugfixes.candleRendererCrashes)
                .addConflict("MixinBlockCandle", SalisConfig.bugfixes.candleRendererCrashes)
                .addConflict("MixinItemShard", SalisConfig.bugfixes.itemShardColor)
                .addConflict("MixinWandManager", SalisConfig.features.useAllBaublesSlots)
                .addConflict("MixinEventHandlerRunic", SalisConfig.features.useAllBaublesSlots)
                .addConflict("MixinWarpEvents_BaubleSlots", SalisConfig.features.useAllBaublesSlots);

        addTransform(thaumicMixinsCompat, "xyz.uniblood.thaumicmixins.mixinplugin.ThaumicMixinsLateMixins");
        addTransform(
            new MethodRemover("func_149719_a", "setBlockBoundsBasedOnState"),
            "thaumcraft.common.blocks.BlockJar",
            "thaumcraft.common.blocks.BlockCustomOre",
            "thaumcraft.common.blocks.BlockArcaneFurnace",
            "thaumcraft.common.blocks.BlockAlchemyFurnace",
            "thaumcraft.common.blocks.BlockHole",
            "thaumcraft.common.blocks.BlockEldritch",
            "thaumcraft.common.blocks.BlockCosmeticSolid",
            "thaumcraft.common.blocks.BlockChestHungry",
            "thaumcraft.common.blocks.BlockCandle");
        addTransform(
            new MethodRemover("func_149633_g", "getSelectedBoundingBoxFromPool"),
            "thaumcraft.common.blocks.BlockAlchemyFurnace",
            "thaumcraft.common.blocks.BlockLoot");
        addTransform(
            new MethodRemover("func_149743_a", "addCollisionBoxesToList"),
            "thaumcraft.common.blocks.BlockJar",
            "thaumcraft.common.blocks.BlockEldritch",
            "thaumcraft.common.blocks.BlockCustomOre");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        ClassTransformer transformer = transformers.get(transformedName);
        if (transformer != null) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, transformer.getFlags());
            reader.accept(transformer.transform(writer), 0);
            return writer.toByteArray();
        }

        return bytes;
    }

    private void addTransform(ClassTransformer transform, String... classes) {
        for (String className : classes) {
            transformers.merge(className, transform, ChainedTransformer::new);
        }
    }
}
