package dev.rndmorris.salisarcana.mixins.early.accessor;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EffectRenderer.class)
public interface AccessorEffectRenderer {

    @Accessor
    ResourceLocation getParticleTextures();
}
