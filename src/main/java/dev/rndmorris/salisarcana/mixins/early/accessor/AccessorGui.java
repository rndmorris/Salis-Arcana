package dev.rndmorris.salisarcana.mixins.early.accessor;

import net.minecraft.client.gui.Gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface AccessorGui {

    @Accessor
    float getZLevel();
}
