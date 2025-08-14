package dev.rndmorris.salisarcana.lib;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import thaumcraft.common.tiles.TileMagicWorkbench;

public final class MundaneCraftingBridge extends InventoryCrafting {

    private final TileMagicWorkbench workbench;

    public MundaneCraftingBridge(TileMagicWorkbench workbench) {
        super(workbench.eventHandler, 3, 3);
        this.workbench = workbench;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index < 9 ? this.workbench.getStackInSlot(index) : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return index < 9 ? this.workbench.decrStackSize(index, count) : null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return index < 9 ? this.workbench.getStackInSlotOnClosing(index) : null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 9) {
            this.workbench.setInventorySlotContents(index, stack);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index < 9;
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        return this.workbench.getStackInRowAndColumn(row, column);
    }
}
