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

    public static final byte RESET_ACTION = (byte) 0;
    public static final byte FORGET_ACTION = (byte) 1;

    Aspect[] aspectsToReset = null;

    int aspectCount = 0; // number of aspects to reset. If zero, reset all aspects.
    byte action = 0;

    public MessageForgetAspects() {}

    public MessageForgetAspects(byte action) {
        this.action = action;
    }

    public MessageForgetAspects(ArrayList<Aspect> aspects, byte action) {
        this.action = action;
        aspectCount = aspects.size();
        aspectsToReset = aspects.toArray(new Aspect[0]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.action = buf.readByte();
        this.aspectCount = buf.readInt();
        if (this.aspectCount > 0) {
            this.aspectCount = buf.readInt();
            this.aspectsToReset = new Aspect[this.aspectCount];
            for (int i = 0; i < this.aspectCount; i++) {
                String tag = ByteBufUtils.readUTF8String(buf);
                this.aspectsToReset[i] = Aspect.getAspect(tag);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.action);
        buf.writeInt(this.aspectCount);
        if (this.aspectCount > 0) {
            buf.writeInt(this.aspectsToReset.length);
            for (Aspect aspect : this.aspectsToReset) {
                ByteBufUtils.writeUTF8String(buf, aspect.getTag());
            }
        }
    }

    @Override
    public IMessage onMessage(MessageForgetAspects message, MessageContext ctx) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        final PlayerKnowledge playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final AspectList playerAspects = playerKnowledge.aspectsDiscovered.get(player.getCommandSenderName());
        if (message.aspectCount == 0) { // reset all
            if (message.action == FORGET_ACTION) {
                playerAspects.aspects.clear();
            } else {
                for (final Aspect aspect : playerAspects.getAspects()) {
                    playerAspects.aspects.put(aspect, 1);
                }
            }
        } else {
            if (message.action == FORGET_ACTION) {
                for (Aspect aspect : message.aspectsToReset) {
                    playerAspects.aspects.remove(aspect);
                }
            } else {
                for (Aspect aspect : message.aspectsToReset) {
                    playerAspects.aspects.put(aspect, 1);
                }
            }
        }
        return null;
    }
}
