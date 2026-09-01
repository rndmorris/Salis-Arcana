package dev.rndmorris.salisarcana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import dev.rndmorris.salisarcana.common.compat.MixinModCompat;
import dev.rndmorris.salisarcana.lib.WandFocusHelper;
import dev.rndmorris.salisarcana.mixins.early.accessor.AccessorCreativeSlot;
import dev.rndmorris.salisarcana.network.MessageQuickStashFocus;
import dev.rndmorris.salisarcana.network.NetworkHandler;

public final class QuickStashFocus {

    public static KeyBinding KEYBIND;

    public static void register() {
        // If none of these mods are present, use the Thaumcraft keybind instead.
        if (MixinModCompat.multiKeyBindsPermitted()) {
            KEYBIND = new KeyBinding(
                "salisarcana.keybind.quick_stash_focus",
                Keyboard.KEY_F,
                "salisarcana.keybind_category");
            ClientRegistry.registerKeyBinding(KEYBIND);
        }

        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandler());
    }

    public static void tryStashSlot(Slot slot, Container container) {
        if (slot == null) return;

        final var mc = Minecraft.getMinecraft();
        final var player = mc.thePlayer;

        if (mc.currentScreen instanceof GuiContainerCreative) {
            // When the Creative GUI is open, the server still thinks that `InventoryContainer` is the open container,
            // so the slot ID won't match up. Therefore, we fix up the parameters to refer to the player's
            // `InventoryContainer` instead.

            if (slot instanceof AccessorCreativeSlot creativeSlot) {
                slot = creativeSlot.salisarcana$getBaseSlot();

                // If the current slot does not refer to the player's inventory, we cannot extract from it since it
                // doesn't exist on the server. (This should only be the "trash can" slot in the Inventory tab.)
                if (slot.inventory != player.inventory) return;

                container = player.inventoryContainer;
                slot = player.inventoryContainer.getSlotFromInventory(player.inventory, slot.getSlotIndex());
            } else {
                // TODO Implement direct extraction from creative / NEI slots for Creative players.
                return;
            }
        }

        if (WandFocusHelper.storeFocusFromSlot(container, slot, player)) {
            NetworkHandler.instance.sendToServer(new MessageQuickStashFocus(slot.slotNumber, container.windowId));
        }
    }

    private static Slot getHeldSlot(EntityPlayer player) {
        final int hotbarIndex = player.inventory.currentItem;

        // Fast path: in vanilla, hotbar slots are slots 36-45.
        Slot slot = player.inventoryContainer.getSlot(36 + hotbarIndex);
        if (slot != null && slot.inventory == player.inventory && slot.getSlotIndex() == hotbarIndex) {
            return slot;
        }

        // Slow path: search through all slots for one that is linked to that hotbar slot.
        return player.inventoryContainer.getSlotFromInventory(player.inventory, hotbarIndex);
    }

    public static final class EventHandler {

        private EventHandler() {}

        @SubscribeEvent
        public void stashInGame(InputEvent.KeyInputEvent event) {
            final var mc = Minecraft.getMinecraft();
            if (!mc.inGameHasFocus || !KEYBIND.isPressed()) return;

            tryStashSlot(getHeldSlot(mc.thePlayer), mc.thePlayer.inventoryContainer);
        }
    }
}
