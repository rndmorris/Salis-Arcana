package dev.rndmorris.salisarcana.network;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewAnalyzer;
import dev.rndmorris.salisarcana.common.infusion.InfusionPreviewInfo;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class MessageRequestInfusionPreview
    implements IMessage, IMessageHandler<MessageRequestInfusionPreview, MessageInfusionPreview> {

    // The client waits two additional ticks to make sure to avoid cooldown.
    private static final int REQUEST_COOLDOWN_TICKS = 10;

    // Comfortably beyond reach, so it only rejects matrices the player can't be looking at.
    private static final double MAX_DISTANCE_SQ = 10 * 10;

    private static final Map<UUID, LastRequest> lastRequestByPlayer = new HashMap<>();

    private static class LastRequest {

        public int tick;
        public MessageInfusionPreview response;

        public LastRequest(int tick, MessageInfusionPreview response) {
            this.tick = tick;
            this.response = response;
        }
    }

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
    public MessageInfusionPreview onMessage(MessageRequestInfusionPreview message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        int currentTick = MinecraftServer.getServer()
            .getTickCounter();
        UUID playerId = player.getUniqueID();
        LastRequest lastRequest = lastRequestByPlayer.get(playerId);
        if (lastRequest != null && currentTick - lastRequest.tick < REQUEST_COOLDOWN_TICKS) {
            // Replay the previous answer rather than leaving the client with nothing, but only for the
            // same matrix - the cached info says nothing about a block the player just switched to.
            return matches(lastRequest.response, message) ? lastRequest.response : null;
        }
        // Stamp the request before validating it, so a client sending junk is throttled just the same.
        lastRequestByPlayer.put(playerId, new LastRequest(currentTick, null));

        // Expired entries can't reject anything anymore, so don't keep them around forever.
        lastRequestByPlayer.values()
            .removeIf(request -> currentTick - request.tick >= REQUEST_COOLDOWN_TICKS);

        if (!InfusionPreviewAnalyzer.canView(player)) return null;

        if (player.getDistanceSq(message.x + 0.5, message.y + 0.5, message.z + 0.5) > MAX_DISTANCE_SQ) return null;

        World world = player.worldObj;
        TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
        if (!(tile instanceof TileInfusionMatrix matrix)) return null;

        InfusionPreviewInfo info = InfusionPreviewAnalyzer.analyze(world, matrix, player);

        MessageInfusionPreview response = new MessageInfusionPreview(message.x, message.y, message.z, info);
        lastRequestByPlayer.put(playerId, new LastRequest(currentTick, response));
        return response;
    }

    private static boolean matches(MessageInfusionPreview response, MessageRequestInfusionPreview message) {
        return response != null && response.x == message.x && response.y == message.y && response.z == message.z;
    }
}
