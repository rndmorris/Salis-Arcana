package dev.rndmorris.salisarcana.network;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.SalisArcana;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class MessageForgetResearch implements IMessage, IMessageHandler<MessageForgetResearch, IMessage> {

    private List<String> researchKeys;

    public MessageForgetResearch() {}

    public MessageForgetResearch(List<String> researchKeys) {
        this.researchKeys = researchKeys;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.researchKeys = new ArrayList<>();
        int length;

        while ((length = buf.readByte() & 0xFF) > 0) {
            byte[] key = new byte[length];
            buf.readBytes(key);
            this.researchKeys.add(new String(key, StandardCharsets.UTF_8));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (final String key : researchKeys) {
            byte[] encoded = key.getBytes(StandardCharsets.UTF_8);
            if (encoded.length == 0) continue;
            if (encoded.length > 255) {
                SalisArcana.LOG.error("Cannot transmit forget-research research key - key is too long.");
                continue;
            }
            buf.writeByte(encoded.length);
            buf.writeBytes(encoded);
        }
        buf.writeByte(0);
    }

    @Override
    public IMessage onMessage(MessageForgetResearch message, MessageContext ctx) {
        final var playerResearch = Thaumcraft.proxy.getPlayerKnowledge().researchCompleted
            .get(Minecraft.getMinecraft().thePlayer.getCommandSenderName());

        playerResearch.removeAll(message.researchKeys);

        return null;
    }
}
