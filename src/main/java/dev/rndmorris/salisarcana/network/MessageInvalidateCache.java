package dev.rndmorris.salisarcana.network;

import net.glease.tc4tweak.modules.FlushableCache;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageInvalidateCache implements IMessage, IMessageHandler<MessageInvalidateCache, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageInvalidateCache message, MessageContext ctx) {
        FlushableCache.enableAll(true);
        return null;
    }
}
