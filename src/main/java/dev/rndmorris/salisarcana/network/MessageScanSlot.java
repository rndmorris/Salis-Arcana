/*
 * Copyright (c) 2015 Christopher "BlayTheNinth" Baker
 * Licensed under the MIT License
 * Taken from ThaumicInventoryScanning
 * https://github.com/GTNewHorizons/ThaumicInventoryScanning
 */

package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageScanSlot implements IMessage {

    private int slotNumber;

    public MessageScanSlot() {}

    public MessageScanSlot(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slotNumber = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slotNumber);
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}
