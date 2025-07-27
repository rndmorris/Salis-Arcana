package dev.rndmorris.salisarcana.network;

import static dev.rndmorris.salisarcana.network.MessageForgetScannedCategory.Category.ENTITIES;
import static dev.rndmorris.salisarcana.network.MessageForgetScannedCategory.Category.NODES;
import static dev.rndmorris.salisarcana.network.MessageForgetScannedCategory.Category.OBJECTS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class MessageForgetScannedCategory implements IMessage, IMessageHandler<MessageForgetScannedCategory, IMessage> {

    private byte categories;

    public MessageForgetScannedCategory() {}

    public MessageForgetScannedCategory(Collection<Category> categories) {
        for (var category : categories) {
            this.categories |= category.bitmask;
        }
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

        if (OBJECTS.isSet(categories)) {
            clearScanMap(playerName, knowledge.objectsScanned);
        }
        if (ENTITIES.isSet(categories)) {
            clearScanMap(playerName, knowledge.entitiesScanned);
        }
        if (NODES.isSet(categories)) {
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

    public enum Category {

        OBJECTS(0b0001),
        ENTITIES(0b0010),
        NODES(0b0100),
        ALL(OBJECTS.bitmask | ENTITIES.bitmask | NODES.bitmask);

        public final byte bitmask;

        Category(int bitmask) {
            this.bitmask = (byte) bitmask;
        }

        public boolean isSet(byte inBits) {
            return bitmask == (inBits & bitmask);
        }
    }
}
