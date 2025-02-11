package dev.rndmorris.salisarcana.network;

import net.glease.tc4tweak.modules.FlushableCache;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * This message is called when tc4tweaks is installed, as it does heavy research caching because Thaumcraft is slow.<br>
 * When we modify researches, we need to invalidate the cache so it'll reflect any changes we make in game.
 */
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
