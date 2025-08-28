package dev.rndmorris.salisarcana.mixins.early.accessor;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface AccessorGuiContainer {

    @Accessor("theSlot")
    Slot getHoveredSlot();

    @Accessor
    int getXSize();

    @Accessor
    int getYSize();

    @Accessor()
    int getGuiLeft();

    @Accessor()
    int getGuiTop();
}
