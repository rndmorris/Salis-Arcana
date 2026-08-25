package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.lib.ArrayHelper.tryGet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import baubles.api.BaublesApi;
import dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container.AccessorContainerFocusPouch;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemFocusPouch;

public class WandFocusHelper {

    public static List<FocusUpgradeType> getAppliedUpgrades(ItemFocusBasic focus, ItemStack itemStack) {
        final var applied = new ArrayList<FocusUpgradeType>(5);
        for (var upgradeId : focus.getAppliedUpgrades(itemStack)) {
            if (upgradeId < 0) {
                break;
            }
            final var tryGetResult = tryGet(FocusUpgradeType.types, upgradeId);
            if (!tryGetResult.success()) {
                break;
            }
            applied.add(tryGetResult.data());
        }
        return applied;
    }

    public static @Nullable ItemFocusBasic getFocusFrom(@Nullable ItemStack heldItem) {
        if (heldItem != null && heldItem.getItem() instanceof ItemFocusBasic focus) {
            return focus;
        }
        return null;
    }

    /**
     * Try to extract a focus from a slot and store it in the player's inventory, prioritizing Baubles over the
     * inventory.
     *
     * @param container The container from where the focus is being extracted.
     * @param slot      The slot of the container from where the focus is being extracted.
     * @param player    The player who is interacting with the container.
     * @return Whether a focus was successfully moved from that slot to a Focus Pouch on the player.
     */
    public static boolean storeFocusFromSlot(Container container, Slot slot, EntityPlayer player) {
        if (slot == null || !slot.canTakeStack(player)) return false;

        final ItemStack focusStack = slot.getStack();
        if (focusStack == null || !(focusStack.getItem() instanceof ItemFocusBasic)) return false;

        // Try to guard against any potential bad IInventory implementations
        final Supplier<ItemStack> extractor = () -> {
            final ItemStack takenStack = slot.decrStackSize(1);
            if (takenStack == null || !(takenStack.getItem() instanceof ItemFocusBasic)) return null;

            slot.onPickupFromSlot(player, takenStack);
            return takenStack;
        };

        // Don't try to store foci into a currently open Focus Pouch
        int skipIdx = -1;
        if (container instanceof AccessorContainerFocusPouch pouchContainer) {
            int blockedSlot = pouchContainer.salisarcana$getBlockSlot();
            skipIdx = container.getSlot(blockedSlot)
                .getSlotIndex();
        }

        // Try to store the focus, prioritizing the focus pouches in the Bauble slots first.
        if (storeFocusInInventory(BaublesApi.getBaubles(player), extractor, -1)
            || storeFocusInInventory(player.inventory, extractor, skipIdx)) {
            container.detectAndSendChanges();
            return true;
        }

        return false;
    }

    /**
     * Place a focus into the first non-full Focus Pouch within this inventory.
     *
     * @param inventory The inventory to scan for Focus Pouches
     * @param focus     A function that extracts the Wand Focus that needs to be stored. Returning null will cancel the
     *                  operation.
     * @param skipIdx   An index of the inventory to skip. Use -1 if no indices should be skipped.
     * @return Whether the focus was successfully added to a Focus Pouch.
     */
    private static boolean storeFocusInInventory(IInventory inventory, Supplier<ItemStack> focus, int skipIdx) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (i == skipIdx) continue;

            final var stack = inventory.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemFocusPouch pouch)) continue;

            final ItemStack[] pouchSlots = pouch.getInventory(stack);
            for (int j = 0; j < pouchSlots.length; j++) {
                if (pouchSlots[j] == null) {
                    // Take the focus from out of the slot
                    final ItemStack focusStack = focus.get();
                    if (focusStack == null) return false;

                    // Put the focus in the pouch
                    pouchSlots[j] = focusStack;
                    pouch.setInventory(stack, pouchSlots);

                    // Update the inventory to inform it that the slot's NBT has changed
                    inventory.setInventorySlotContents(i, stack);

                    return true;
                }
            }
        }

        return false;
    }
}
