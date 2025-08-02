package dev.rndmorris.salisarcana.network;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.BitField;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class MessageForgetScannedCategory implements IMessage, IMessageHandler<MessageForgetScannedCategory, IMessage> {

    private final BitField objectsMask = new BitField(0b0001);
    private final BitField entitiesMask = new BitField(0b0010);
    private final BitField nodesMask = new BitField(0b0100);

    private int categories = 0;

    public MessageForgetScannedCategory() {}

    public MessageForgetScannedCategory(boolean objects, boolean entities, boolean nodes) {
        int categories = 0;
        if (objects) {
            categories = objectsMask.set(categories);
        }
        if (entities) {
            categories = entitiesMask.set(categories);
        }
        if (nodes) {
            categories = nodesMask.set(categories);
        }
        this.categories = categories;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        categories = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(categories);
    }

    @Override
    public IMessage onMessage(MessageForgetScannedCategory message, MessageContext ctx) {
        if (message.categories == 0) {
            return null;
        }

        final var categories = message.categories;
        final var knowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final var playerName = Minecraft.getMinecraft().thePlayer.getCommandSenderName();

        if (objectsMask.isSet(categories)) {
            clearScanMap(playerName, knowledge.objectsScanned);
        }
        if (entitiesMask.isSet(categories)) {
            clearScanMap(playerName, knowledge.entitiesScanned);
        }
        if (nodesMask.isSet(categories)) {
            clearScanMap(playerName, knowledge.phenomenaScanned);
        }

        return null;
    }

    private void clearScanMap(String playerName, Map<String, ArrayList<String>> map) {
        final var scannedList = map.get(playerName);
        if (scannedList != null) {
            scannedList.clear();
        }
    }
}
