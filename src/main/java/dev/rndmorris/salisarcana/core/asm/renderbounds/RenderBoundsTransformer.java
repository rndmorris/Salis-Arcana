package dev.rndmorris.salisarcana.core.asm.renderbounds;

import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;

public class RenderBoundsTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.startsWith("thaumcraft.client.renderers.block.")) {
            final var reader = new ClassReader(basicClass);
            final var writer = new ClassWriter(reader, 0);
            final var visitor = new RenderBoundsClassVisitor(writer);

            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        return basicClass;
    }
}
