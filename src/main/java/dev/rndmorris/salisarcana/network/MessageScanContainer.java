package dev.rndmorris.salisarcana.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.InventoryHelper;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.research.ResearchManager;

public class MessageScanContainer implements IMessage, IMessageHandler<MessageScanContainer, IMessage> {

    public int x, y, z;

    public MessageScanContainer() {}

    public MessageScanContainer(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(MessageScanContainer message, MessageContext ctx) {
        EntityPlayerMP entityPlayer = ctx.getServerHandler().playerEntity;
        // we do this check serverside since the client can lie
        if (SalisConfig.features.thaumometerScanContainersResearch.isEnabled()) {
            if (!ResearchManager.isResearchComplete(
                entityPlayer.getCommandSenderName(),
                SalisConfig.features.thaumometerScanContainersResearch.getInternalName())) {
                return null;
            }
        }

        World world = entityPlayer.worldObj;
        TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
        if (tile instanceof IInventory inventory) {
            InventoryHelper.scanInventory(inventory, entityPlayer);
        }
        PacketHandler.INSTANCE.sendTo(
            new PacketSyncScannedItems(ctx.getServerHandler().playerEntity),
            ctx.getServerHandler().playerEntity);
        return null;
    }
}
