/*
 * Copyright (c) 2015 Christopher "BlayTheNinth" Baker
 * Licensed under the MIT License
 * Taken from ThaumicInventoryScanning
 * https://github.com/GTNewHorizons/ThaumicInventoryScanning
 */

package dev.rndmorris.salisarcana.network;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

public class HandlerScanSelf implements IMessageHandler<MessageScanSelf, IMessage> {

    @Override
    public IMessage onMessage(MessageScanSelf message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        ScanResult scan = new ScanResult((byte) 2, 0, 0, entityPlayer, "");
        ScanManager.completeScan(entityPlayer, scan, "@");
        return null;
    }
}
