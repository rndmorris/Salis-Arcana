package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.client.infusion.InfusionPreview;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewInfo;
import io.netty.buffer.ByteBuf;

public class MessageInfusionPreview implements IMessage, IMessageHandler<MessageInfusionPreview, IMessage> {

    public int x, y, z;
    public InfusionPreviewInfo info;

    public MessageInfusionPreview() {}

    public MessageInfusionPreview(int x, int y, int z, InfusionPreviewInfo info) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.info = info;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        info = new InfusionPreviewInfo();
        info.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        info.toBytes(buf);
    }

    @Override
    public IMessage onMessage(MessageInfusionPreview message, MessageContext ctx) {
        InfusionPreview.onPreviewReceived(message.x, message.y, message.z, message.info);
        return null;
    }
}
