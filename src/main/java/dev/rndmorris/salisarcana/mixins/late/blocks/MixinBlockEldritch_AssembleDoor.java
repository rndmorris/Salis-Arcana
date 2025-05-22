package dev.rndmorris.salisarcana.mixins.late.blocks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.blocks.BlockEldritch;

@Mixin(BlockEldritch.class)
public class MixinBlockEldritch_AssembleDoor {
    @Unique
    private static final byte[][] DOOR_STRUCTURE = new byte[][] {
        new byte[] {0, 0, 1, 1, 1, 0, 0},
        new byte[] {0, 1, 2, 2, 2, 1, 0},
        new byte[] {1, 2, 2, 2, 2, 2, 1},
        new byte[] {1, 2, 2, 0, 2, 2, 1},
        new byte[] {1, 2, 2, 2, 2, 2, 1},
        new byte[] {0, 1, 2, 2, 2, 1, 0},
        new byte[] {0, 0, 1, 1, 1, 0, 0}
    };

    @WrapMethod(method = "onBlockActivated")
    public boolean assembleDoor(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9, Operation<Boolean> original) {
        if(player.capabilities.isCreativeMode && world.getBlockMetadata(x, y, z) == 8 && player.isSneaking()) {

        }

        return original.call(world, x, y, z, player, side, par7, par8, par9);
    }

    @Unique
    private boolean testDoor(int x, int y, int z, boolean facingZ) {
        for(int v = 0; v < 7; v++) {
            for(int h = 0; h < 7; h++) {
                if(DOOR_STRUCTURE[v][h] == 1) {

                } else if (DOOR_STRUCTURE[v][h] == 2) {

                }
            }
        }

        return true;
    }
}
