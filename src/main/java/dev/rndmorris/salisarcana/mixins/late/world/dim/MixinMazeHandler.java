package dev.rndmorris.salisarcana.mixins.late.world.dim;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.lib.Maze;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.MazeHandler;

@Mixin(value = MazeHandler.class, remap = false)
public class MixinMazeHandler {

    /**
     * @author Midnight145
     * @reason The current data stored in labyrinth.dat is inefficient, so we modify the stored data structure which is
     *         not compatible with the current MazeThread implementation.
     */
    @Overwrite
    private static void readNBT(NBTTagCompound nbt) {
        Maze.readNBT(nbt);
    }

    /**
     * @author Midnight145
     * @reason compat with new format
     */
    @Overwrite
    private static NBTTagCompound writeNBT() {
        return Maze.writeNBT(2);
    }

    /**
     * @author Midnight145
     * @reason compat with new format
     */
    @Overwrite
    public static boolean mazesInRange(int chunkX, int chunkZ, int w, int h) {
        for (int x = -w; x <= w; ++x) {
            for (int z = -h; z <= h; ++z) {
                if (Maze.intersects(chunkX + x, chunkZ + z)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Inject(
        method = "generateEldritch",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/world/dim/MazeHandler;getFromHashMap(Lthaumcraft/common/lib/world/dim/CellLoc;)Lthaumcraft/common/lib/world/dim/Cell;",
            shift = At.Shift.AFTER))
    private static void modifyCell(World world, Random random, int cx, int cz, CallbackInfo ci,
        @Local(name = "cell") LocalRef<Cell> cell) {
        Maze maze = Maze.getMazeAtPoint(cx, cz);
        cell.set(new Cell(maze.getRoom(cx, cz)));
    }

}
