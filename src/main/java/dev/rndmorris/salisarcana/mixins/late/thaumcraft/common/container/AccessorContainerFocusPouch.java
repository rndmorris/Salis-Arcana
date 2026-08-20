package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import thaumcraft.common.container.ContainerFocusPouch;

@Mixin(value = ContainerFocusPouch.class, remap = false)
public interface AccessorContainerFocusPouch {

    @Accessor("blockSlot")
    int salisarcana$getBlockSlot();
}
