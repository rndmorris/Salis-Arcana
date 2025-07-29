package dev.rndmorris.salisarcana.network;

import java.nio.charset.StandardCharsets;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;

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
        if (!SalisConfig.features.nomiconDuplicateResearch.isEnabled()) {
            // The research duplication feature is disabled on the server.
            return null;
        }

        final var player = ctx.getServerHandler().playerEntity;
        final String username = player.getCommandSenderName();

        final boolean freeScribestuff = SalisConfig.features.creativeOpThaumonomicon.isEnabled()
            && player.capabilities.isCreativeMode;
        final boolean freeAspects = freeScribestuff || SalisConfig.features.researchDuplicationFree.isEnabled();

        if (!ResearchManager.isResearchComplete(username, message.key)
            || !ResearchManager.isResearchComplete(username, "RESEARCHDUPE")) {
            // The player does not have enough knowledge to duplicate the research.
            return null;
        }

        AspectList aspectPrice = null;
        if (!freeAspects) {
            aspectPrice = ResearchCategories.getResearch(message.key).tags;

            if (!ResearchHelper.hasResearchAspects(username, aspectPrice)) {
                // The player does not have sufficient research aspects to duplicate the research.
                return null;
            }
        }

        if (!freeScribestuff) {
            if (!ResearchManager.consumeInkFromPlayer(player, false)) {
                // The player does not have sufficient ink to duplicate the research.
                return null;
            }

            // Spend the paper
            if (!player.inventory.consumeInventoryItem(Items.paper)) {
                // The player does not have sufficient paper to duplicate the research.
                return null;
            }

            // The player *is* able to duplicate the research.
            // Spend the ink
            ResearchManager.consumeInkFromPlayer(player, true);
        }

        if (!freeAspects) {
            // Spend the research aspects
            final var playerAspects = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(username);
            for (final var aspect : aspectPrice.aspects.entrySet()) {
                playerAspects.reduce(aspect.getKey(), aspect.getValue());
            }
        }

        // Create the research note.
        final var note = ResearchManager
            .createNote(new ItemStack(ConfigItems.itemResearchNotes, 1, 64), message.key, player.worldObj);
        if (note != null) {
            note.getTagCompound()
                .setBoolean("complete", true);
            if (!player.inventory.addItemStackToInventory(note)) {
                player.dropPlayerItemWithRandomChoice(note, false);
            }
            player.worldObj.playSoundAtEntity(player, "thaumcraft:learn", 0.75F, 1.0F);
        }

        player.inventoryContainer.detectAndSendChanges();

        return null;
    }
}
