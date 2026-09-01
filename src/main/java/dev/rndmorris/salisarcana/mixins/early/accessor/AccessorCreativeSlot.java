package dev.rndmorris.salisarcana.mixins.early.accessor;

import net.minecraft.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.client.gui.inventory.GuiContainerCreative$CreativeSlot")
public interface AccessorCreativeSlot {

    @Accessor("field_148332_b")
    Slot salisarcana$getBaseSlot();
}
