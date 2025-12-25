package dev.rndmorris.salisarcana.network;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class MessageForgetScannedObjects implements IMessage, IMessageHandler<MessageForgetScannedObjects, IMessage> {

    private HashSet<Integer> forgetHashes;

    public MessageForgetScannedObjects() {}

    public MessageForgetScannedObjects(Collection<Integer> forgetHashes) {
        this.forgetHashes = new HashSet<>(forgetHashes.size());
        this.forgetHashes.addAll(forgetHashes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        final var size = buf.readInt();
        forgetHashes = new HashSet<>(size);
        for (var count = 0; count < size; ++count) {
            forgetHashes.add(buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(forgetHashes.size());
        for (var hash : forgetHashes) {
            // we can theoretically run into the max packet size doing
            // this, but that would require so many unique item hashes
            // that I don't think it's a practical concern
            buf.writeInt(hash);
        }
    }

    @Override
    public IMessage onMessage(MessageForgetScannedObjects message, MessageContext ctx) {
        final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var playerName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
        final var objectsScanned = knowledge.objectsScanned.get(playerName);

        if (objectsScanned == null || objectsScanned.size() == 0) {
            return null;
        }

        for (var hash : message.forgetHashes) {
            final var key = "@" + hash;
            objectsScanned.remove(key);
        }

        return null;
    }
}
