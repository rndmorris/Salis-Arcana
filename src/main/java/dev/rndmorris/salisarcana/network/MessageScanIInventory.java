package dev.rndmorris.salisarcana.network;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.research.ScanManager;

public class MessageScanIInventory implements IMessage, IMessageHandler<MessageScanIInventory, IMessage> {

    int id, meta;

    public MessageScanIInventory() {}

    public MessageScanIInventory(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        meta = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(meta);
    }

    @Override
    public IMessage onMessage(MessageScanIInventory message, MessageContext ctx) {
        ScanResult sr = new ScanResult((byte) 1, message.id, message.meta, null, "");
        ItemStack item = new ItemStack(Item.getItemById(message.id), 1, message.meta);
        if (ResearchHelper.isItemScanned(ctx.getServerHandler().playerEntity, item, "@")) {
            return null;
        }
        ScanManager.completeScan(ctx.getServerHandler().playerEntity, sr, "@");
        PacketHandler.INSTANCE.sendTo(
            new PacketSyncScannedItems(ctx.getServerHandler().playerEntity),
            ctx.getServerHandler().playerEntity);
        return null;
    }
}
