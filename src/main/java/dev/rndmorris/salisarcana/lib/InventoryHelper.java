package dev.rndmorris.salisarcana.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

public class InventoryHelper {

    public static void scanInventory(IInventory inventory, EntityPlayer player) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if (item == null) {
                continue;
            }
            ScanResult result = new ScanResult(
                (byte) 1,
                Item.getIdFromItem(item.getItem()),
                item.getItemDamage(),
                null,
                "");

            if (ResearchHelper.isItemScanned(player, item, "@")) {
                continue;
            }
            ScanManager.completeScan(player, result, "@");
        }
    }
}
