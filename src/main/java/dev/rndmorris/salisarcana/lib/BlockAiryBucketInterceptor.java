package dev.rndmorris.salisarcana.lib;

import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.config.ConfigBlocks;

public class BlockAiryBucketInterceptor {

    @SubscribeEvent
    public void onBucketClicked(FillBucketEvent event) {
        if (event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = event.target.blockX;
            int y = event.target.blockY;
            int z = event.target.blockZ;

            switch (event.target.sideHit) {
                case 0:
                    y--;
                    break;
                case 1:
                    y++;
                    break;
                case 2:
                    z--;
                    break;
                case 3:
                    z++;
                    break;
                case 4:
                    x--;
                    break;
                case 5:
                    x++;
                    break;
            }

            if (event.world.getBlock(x, y, z) == ConfigBlocks.blockAiry
                && !ConfigBlocks.blockAiry.isAir(event.world, x, y, z)) {
                event.setCanceled(true);
            }
        }
    }
}
