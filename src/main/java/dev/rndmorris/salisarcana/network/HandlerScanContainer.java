package dev.rndmorris.salisarcana.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

public class HandlerScanContainer implements IMessageHandler<MessageScanContainer, IMessage> {

    @Override
    public IMessage onMessage(MessageScanContainer message, MessageContext ctx) {
        EntityPlayerMP entityPlayer = ctx.getServerHandler().playerEntity;
        // we do this check serverside since the client can lie
        if (ConfigModuleRoot.enhancements.thaumometerScanContainersResearch.isEnabled()) {
            if (!ResearchManager.isResearchComplete(
                entityPlayer.getCommandSenderName(),
                "salisarcana:" + ConfigModuleRoot.enhancements.thaumometerScanContainersResearch.researchName)) {
                return null;
            }
        }

        World world = entityPlayer.worldObj;
        TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
        if (tile instanceof IInventory inventory) {
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
                if (ScanManager.isValidScanTarget(entityPlayer, result, "@")
                    && !ScanManager.getScanAspects(result, entityPlayer.worldObj).aspects.isEmpty()) {
                    ScanManager.completeScan(entityPlayer, result, "@");

                }
            }
        }
        return null;
    }

}
