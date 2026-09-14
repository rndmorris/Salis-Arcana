package dev.rndmorris.salisarcana.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

public final class EventUtils {

    private EventUtils() {}

    public static boolean canBreakBlock(World world, int x, int y, int z, EntityPlayer player) {
        return canBreakBlock(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), player);
    }

    public static boolean canBreakBlock(World world, int x, int y, int z, Block block, int meta, EntityPlayer player) {
        return world.canMineBlock(player, x, y, z)
            && !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(x, y, z, world, block, meta, player));
    }

    public static boolean tryPlaceBlock(World world, int x, int y, int z, Block block, int meta, int flags,
        EntityPlayer player) {

        /// This code is based on {@link net.minecraftforge.common.ForgeHooks#onPlaceItemIntoWorld}.
        final var snapshot = BlockSnapshot.getBlockSnapshot(world, x, y, z, flags);
        final boolean couldPlace = world.setBlock(x, y, z, block, meta, 0);
        if (!couldPlace) return false;

        final var placeEvent = new BlockEvent.PlaceEvent(snapshot, snapshot.replacedBlock, player);
        if (MinecraftForge.EVENT_BUS.post(placeEvent)) {
            world.restoringBlockSnapshots = true;
            snapshot.restore(true, false);
            world.restoringBlockSnapshots = false;
            return false;
        }

        if (block != null && !block.hasTileEntity(meta)) {
            block.onBlockAdded(world, x, y, z);
        }

        world.markAndNotifyBlock(x, y, z, null, snapshot.replacedBlock, block, snapshot.flag);

        return true;
    }
}
