package dev.rndmorris.salisarcana.network;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.PlayerKnowledge;

public class MessageForgetAspects implements IMessage, IMessageHandler<MessageForgetAspects, IMessage> {

    Aspect[] aspectsToForget = null;

    int forgetCount = 0; // number of aspects to reset. If zero, reset all aspects.

    public MessageForgetAspects() {

    }

    public MessageForgetAspects(ArrayList<Aspect> aspects) {
        forgetCount = aspects.size();
        aspectsToForget = aspects.toArray(new Aspect[0]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.forgetCount = buf.readInt();
        if (this.forgetCount > 0) {
            this.forgetCount = buf.readInt();
            this.aspectsToForget = new Aspect[this.forgetCount];
            for (int i = 0; i < this.forgetCount; i++) {
                String tag = ByteBufUtils.readUTF8String(buf);
                this.aspectsToForget[i] = Aspect.getAspect(tag);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.forgetCount);
        if (this.forgetCount > 0) {
            buf.writeInt(this.aspectsToForget.length);
            for (Aspect aspect : this.aspectsToForget) {
                ByteBufUtils.writeUTF8String(buf, aspect.getTag());
            }
        }
    }

    @Override
    public IMessage onMessage(MessageForgetAspects message, MessageContext ctx) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        final PlayerKnowledge playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final AspectList playerAspects = playerKnowledge.aspectsDiscovered.get(player.getCommandSenderName());
        if (message.forgetCount == 0) { // reset all
            playerAspects.aspects.clear();
        } else {
            for (Aspect aspect : message.aspectsToForget) {
                playerAspects.aspects.remove(aspect);
            }
        }
        return null;
    }
}
