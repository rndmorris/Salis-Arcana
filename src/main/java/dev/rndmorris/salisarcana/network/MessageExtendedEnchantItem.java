package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageExtendedEnchantItem implements IMessage, IMessageHandler<MessageExtendedEnchantItem, IMessage> {

    private int button;

    public MessageExtendedEnchantItem() {}

    public MessageExtendedEnchantItem(int button) {
        this.button = button;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.button = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.button);
    }

    @Override
    public IMessage onMessage(MessageExtendedEnchantItem message, MessageContext ctx) {
        final var player = ctx.getServerHandler().playerEntity;

        if (player.openContainer != null) {
            player.openContainer.enchantItem(player, message.button);
        }

        return null;
    }
}
