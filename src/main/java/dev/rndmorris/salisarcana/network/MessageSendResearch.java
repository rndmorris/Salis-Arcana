package dev.rndmorris.salisarcana.network;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import com.google.gson.GsonBuilder;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.settings.ResearchEntry;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import io.netty.buffer.ByteBuf;

public class MessageSendResearch implements IMessage, IMessageHandler<MessageSendResearch, IMessage> {

    private ResearchEntry research;

    public MessageSendResearch() {

    }

    public MessageSendResearch(ResearchEntry research) {
        System.out.println("Sending research: " + research.getKey());
        this.research = research;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String json = new String(buf.array());
        String parsed = json.substring(json.indexOf('{'), json.lastIndexOf('}') + 1); // remove any weird characters
        ResearchEntry research = ResearchHelper.loadResearchFromJson(parsed);
        if (research == null) {
            LOG.error("Failed to parse research from message: {}", json);
            return;
        }
        LOG.info("Received research: {}", research.getKey());
        if (!ResearchHelper.registerCustomResearch(parsed)) {
            LOG.error("Failed to register research from message: {}", json);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // we don't use ResearchHelper here because we don't want to pretty-print to save space
        String json = new GsonBuilder().create()
            .toJson(research, ResearchEntry.class);
        buf.writeBytes(json.getBytes());
    }

    @Override
    public IMessage onMessage(MessageSendResearch message, MessageContext ctx) {
        return null;
    }
}
