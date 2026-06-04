package dev.rndmorris.salisarcana.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewAnalyzer;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewInfo;
import dev.rndmorris.salisarcana.config.SalisConfig;
import io.netty.buffer.ByteBuf;
import thaumcraft.api.IGoggles;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class MessageRequestInfusionPreview
    implements IMessage, IMessageHandler<MessageRequestInfusionPreview, IMessage> {

    private static final int REQUEST_COOLDOWN_TICKS = 2;

    // Squared player→matrix distance cap. The matrix scan reaches 12 blocks, so 32² leaves headroom
    // while keeping us from servicing requests for matrices the player can't plausibly be looking at.
    private static final double MAX_DISTANCE_SQ = 32 * 32;
    private static final Map<UUID, Long> lastRequestTickByPlayer = new HashMap<>();

    public int x, y, z;

    public MessageRequestInfusionPreview() {}

    public MessageRequestInfusionPreview(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(MessageRequestInfusionPreview message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        if (!SalisConfig.features.infusionPreview.isEnabled()) return null;
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "salisarcana:INFUSION_PREVIEW"))
            return null;

        ItemStack helmet = player.inventory.armorItemInSlot(3);
        if (helmet == null) return null;
        Item helmetItem = helmet.getItem();
        if (!(helmetItem instanceof IGoggles goggles) || !goggles.showIngamePopups(helmet, player)) return null;

        double dx = message.x + 0.5 - player.posX;
        double dy = message.y + 0.5 - player.posY;
        double dz = message.z + 0.5 - player.posZ;
        if (dx * dx + dy * dy + dz * dz > MAX_DISTANCE_SQ) return null;

        World world = player.worldObj;
        TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
        if (!(tile instanceof TileInfusionMatrix matrix)) return null;

        long worldTime = world.getTotalWorldTime();
        UUID playerId = player.getUniqueID();
        Long lastRequestTick = lastRequestTickByPlayer.get(playerId);
        if (lastRequestTick != null && worldTime - lastRequestTick < REQUEST_COOLDOWN_TICKS) return null;
        lastRequestTickByPlayer.put(playerId, worldTime);

        InfusionPreviewInfo info = InfusionPreviewAnalyzer.analyze(world, matrix, player);

        NetworkHandler.instance.sendTo(new MessageInfusionPreview(message.x, message.y, message.z, info), player);
        return null;
    }
}
