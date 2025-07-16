package dev.rndmorris.salisarcana.network;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.PlayerKnowledge;

public class MessageResetAspects implements IMessage, IMessageHandler<MessageResetAspects, IMessage> {

    Aspect[] aspectsToReset = null;

    int resetCount = 0; // number of aspects to reset. If zero, reset all aspects.

    public MessageResetAspects() {

    }

    public MessageResetAspects(ArrayList<Aspect> aspects) {
        resetCount = aspects.size();
        aspectsToReset = aspects.toArray(new Aspect[0]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.resetCount = buf.readInt();
        if (this.resetCount > 0) {
            this.resetCount = buf.readInt();
            this.aspectsToReset = new Aspect[this.resetCount];
            for (int i = 0; i < this.resetCount; i++) {
                String tag = ByteBufUtils.readUTF8String(buf);
                this.aspectsToReset[i] = Aspect.getAspect(tag);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.resetCount);
        if (this.resetCount > 0) {
            buf.writeInt(this.aspectsToReset.length);
            for (Aspect aspect : this.aspectsToReset) {
                ByteBufUtils.writeUTF8String(buf, aspect.getTag());
            }
        }
    }

    @Override
    public IMessage onMessage(MessageResetAspects message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        final PlayerKnowledge playerKnowledge = Thaumcraft.proxy.getPlayerKnowledge();
        final AspectList playerAspects = playerKnowledge.aspectsDiscovered.get(player.getCommandSenderName());
        if (message.resetCount == 0) { // reset all
            for (final Aspect aspect : playerAspects.getAspects()) {
                playerAspects.aspects.put(aspect, 1);
            }
        } else {
            for (Aspect aspect : this.aspectsToReset) {
                playerAspects.aspects.put(aspect, 1);
            }
        }
        return null;
    }
}
