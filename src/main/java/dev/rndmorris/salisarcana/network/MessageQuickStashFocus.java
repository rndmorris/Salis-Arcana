package dev.rndmorris.salisarcana.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.WandFocusHelper;
import io.netty.buffer.ByteBuf;

public class MessageQuickStashFocus implements IMessage, IMessageHandler<MessageQuickStashFocus, IMessage> {

    private int windowId;
    private int slot;

    public MessageQuickStashFocus() {}

    public MessageQuickStashFocus(int slot, int windowId) {
        this.slot = slot;
        this.windowId = windowId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.windowId = buf.readInt();
        this.slot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.windowId);
        buf.writeInt(this.slot);
    }

    @Override
    public IMessage onMessage(MessageQuickStashFocus message, MessageContext ctx) {
        if (!SalisConfig.features.quickStashFocus.isEnabled()) return null;

        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        final Container container = player.openContainer;

        if (container.windowId != message.windowId) return null;
        if (message.slot < 0 || message.slot >= container.inventorySlots.size()) return null;

        final Slot slot = container.getSlot(message.slot);

        if (WandFocusHelper.storeFocusFromSlot(container, slot, player)) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:cameraticks", 0.3F, 1.0F);
        }

        return null;
    }

}
