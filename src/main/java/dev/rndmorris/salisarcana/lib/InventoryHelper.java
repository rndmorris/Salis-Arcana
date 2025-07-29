package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

public class InventoryHelper {

    /**
     * Get the non-null items in an {@link IInventory} as an {@link ArrayList}.
     *
     * @param inventory The inventory on which to operate.
     * @return An {@link ArrayList} of the non-{@code null} items in {@code inventory}.
     */
    public static ArrayList<ItemStack> getItemStacks(IInventory inventory) {
        return getItemStacks(inventory, null);
    }

    /**
     * Get the non-null items in an {@link IInventory}, that meet the provided predicate, as an {@link ArrayList}.
     *
     * @param inventory The inventory on which to operate.
     * @param predicate The test that determines if the item should be included in the results. Null items will be
     *                  rejected without being tested.
     * @return An {@link ArrayList} of the non-{@code null} items in {@code inventory} that meet the {@code predicate},
     *         or all non-{@code null} items in {@code inventory} if {@code predicate} is {@code null}.
     */
    public static ArrayList<ItemStack> getItemStacks(IInventory inventory, @Nullable Predicate<ItemStack> predicate) {
        Objects.requireNonNull(inventory);
        final var output = new ArrayList<ItemStack>(inventory.getSizeInventory());
        for (var index = 0; index < inventory.getSizeInventory(); ++index) {
            ItemStack item = inventory.getStackInSlot(index);
            if (item == null) {
                continue;
            }
            if (predicate == null || predicate.test(item)) {
                output.add(item);
            }
        }
        return output;
    }

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
