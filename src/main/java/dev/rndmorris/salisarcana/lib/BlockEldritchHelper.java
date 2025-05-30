package dev.rndmorris.salisarcana.lib;

import cpw.mods.fml.common.network.NetworkRegistry;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.tiles.TileEldritchLock;

public class BlockEldritchHelper {
    public static final byte[][] DOOR_STRUCTURE = new byte[][] {
        new byte[] {0, 0, 1, 1, 1, 0, 0},
        new byte[] {0, 1, 2, 2, 2, 1, 0},
        new byte[] {1, 2, 2, 2, 2, 2, 1},
        new byte[] {1, 2, 2, 0, 2, 2, 1},
        new byte[] {1, 2, 2, 2, 2, 2, 1},
        new byte[] {0, 1, 2, 2, 2, 1, 0},
        new byte[] {0, 0, 1, 1, 1, 0, 0}
    };

    public static void destroyDoor(TileEldritchLock doorLock) {
        final var world = doorLock.getWorldObj();
        if(world.isRemote) return;

        final boolean facingX = doorLock.getFacing() > 3;
        for(int v = -2; v <= 2; v++) {
            for (int h = -2; h <= 2; h++) {
                int x = doorLock.xCoord + (facingX ? 0 : h);
                int y = doorLock.yCoord + v;
                int z = doorLock.zCoord + (facingX ? h : 0);

                if(world.getBlock(x, y, z) == ConfigBlocks.blockAiry && world.getBlockMetadata(x, y, z) == 12) {
                    PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y, z, 4194368), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32d));
                    world.setBlockToAir(x, y, z);
                }
            }
        }

        world.setBlockToAir(doorLock.xCoord, doorLock.yCoord, doorLock.zCoord);
    }
}
