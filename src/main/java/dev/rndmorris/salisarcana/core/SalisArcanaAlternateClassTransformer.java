package dev.rndmorris.salisarcana.core;

import java.util.HashMap;

import dev.rndmorris.salisarcana.core.asm.renderbounds.CollisionBoxesClassVisitor;
import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassWriter;

import dev.rndmorris.salisarcana.core.asm.IClassVisitorFactory;
import dev.rndmorris.salisarcana.core.asm.renderbounds.RendererBoundsClassVisitor;

public class SalisArcanaAlternateClassTransformer implements IClassTransformer {

    private static final HashMap<String, IClassVisitorFactory> transforms = new HashMap<>();

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;
        if (!transformedName.startsWith("thaumcraft.")) return bytes;

        final var visitor = transforms.get(transformedName);
        if (visitor != null) {
            final var reader = new ClassReader(bytes);
            final var writer = new ClassWriter(reader, 0);
            final var transformer = visitor.transform(writer);
            reader.accept(transformer, 0);
            return writer.toByteArray();
        } else {
            return bytes;
        }
    }

    public static void initialize() {
        // TODO Add config setting here
        if (true) {
            final IClassVisitorFactory renderBoundsFixer = RendererBoundsClassVisitor::new;
            addTransformer("thaumcraft.client.renderers.block.BlockArcaneFurnaceRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockCandleRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockCosmeticOpaqueRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockCustomOreRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockEldritchRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockEssentiaReservoirRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockGasRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockJarRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockLifterRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockLootCrateRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockLootUrnRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockMetalDeviceRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockStoneDeviceRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockTaintFibreRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockTaintRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockTubeRenderer", renderBoundsFixer);
            addTransformer("thaumcraft.client.renderers.block.BlockWoodenDeviceRenderer", renderBoundsFixer);

            final IClassVisitorFactory collisionBoxesFixer = CollisionBoxesClassVisitor::new;
            addTransformer("thaumcraft.common.blocks.BlockAiry", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockAlchemyFurnace", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockArcaneFurnace", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockMetalDevice", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockStoneDevice", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockTube", collisionBoxesFixer);
            addTransformer("thaumcraft.common.blocks.BlockWoodenDevice", collisionBoxesFixer);
        }
    }

    public static void addTransformer(String className, IClassVisitorFactory transformer) {
        transforms.merge(className, transformer, ClassVisitorChain::new);
    }

    private static class ClassVisitorChain implements IClassVisitorFactory {

        private final IClassVisitorFactory first;
        private final IClassVisitorFactory second;

        public ClassVisitorChain(IClassVisitorFactory first, IClassVisitorFactory second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public ClassVisitor transform(ClassVisitor output) {
            return first.transform(second.transform(output));
        }
    }
}
