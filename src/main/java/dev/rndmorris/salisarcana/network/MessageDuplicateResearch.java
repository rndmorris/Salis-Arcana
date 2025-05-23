package dev.rndmorris.salisarcana.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;

import java.nio.charset.StandardCharsets;

public class MessageDuplicateResearch implements IMessage, IMessageHandler<MessageDuplicateResearch, IMessage> {
    private String key;

    public MessageDuplicateResearch() {}

    public MessageDuplicateResearch(String key) {
        this.key = key;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        final int length = buf.readInt();
        final byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        this.key = new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        final byte[] bytes = this.key.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public IMessage onMessage(MessageDuplicateResearch message, MessageContext ctx) {
        final var player = ctx.getServerHandler().playerEntity;
        if(SalisConfig.features.nomiconDuplicateResearch.isEnabled()
            && ResearchManager.isResearchComplete(player.getCommandSenderName(), message.key)
            && ResearchManager.isResearchComplete(player.getCommandSenderName(), "RESEARCHDUPE")
            && ResearchHelper.consumeScribestuff(player)) {

            final var note = ResearchManager.createNote(new ItemStack(ConfigItems.itemResearchNotes, 1, 64), message.key, player.worldObj);
            if(note != null) {
                note.getTagCompound().setBoolean("complete", true);
                if (!player.inventory.addItemStackToInventory(note)) {
                    player.dropPlayerItemWithRandomChoice(note, false);
                }
                player.worldObj.playSoundAtEntity(player, "thaumcraft:learn", 0.75F, 1.0F);
            }

            player.inventoryContainer.detectAndSendChanges();
        }
        return null;
    }
}
