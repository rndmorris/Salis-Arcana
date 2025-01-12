/*
 * Copyright (c) 2015 Christopher "BlayTheNinth" Baker
 * Licensed under the MIT License
 * Taken from ThaumicInventoryScanning
 * https://github.com/GTNewHorizons/ThaumicInventoryScanning
 */

package dev.rndmorris.salisarcana.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

public class HandlerScanSlot implements IMessageHandler<MessageScanSlot, IMessage> {

    @Override
    public IMessage onMessage(MessageScanSlot message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        Container container = entityPlayer.openContainer;
        if (container != null && message.getSlotNumber() >= 0
            && message.getSlotNumber() < container.inventorySlots.size()) {
            Slot slot = container.inventorySlots.get(message.getSlotNumber());
            if (slot.getStack() != null && slot.canTakeStack(entityPlayer) && !(slot instanceof SlotCrafting)) {
                ItemStack itemStack = slot.getStack();
                ScanResult scan = new ScanResult(
                    (byte) 1,
                    Item.getIdFromItem(itemStack.getItem()),
                    itemStack.getItemDamage(),
                    null,
                    "");
                ScanManager.completeScan(entityPlayer, scan, "@");
            }
        }
        return null;
    }
}
