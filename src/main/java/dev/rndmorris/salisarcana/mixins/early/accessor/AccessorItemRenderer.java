package dev.rndmorris.salisarcana.mixins.early.accessor;

import net.minecraft.client.renderer.ItemRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface AccessorItemRenderer {

    @Accessor
    float getEquippedProgress();

    @Accessor
    float getPrevEquippedProgress();
}
