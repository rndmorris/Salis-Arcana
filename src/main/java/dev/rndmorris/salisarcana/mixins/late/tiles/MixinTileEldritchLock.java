package dev.rndmorris.salisarcana.mixins.late.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.Maze;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(value = TileEldritchLock.class, remap = false)
public class MixinTileEldritchLock {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
        method = "doBossSpawn",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/world/dim/MazeHandler;getFromHashMap(Lthaumcraft/common/lib/world/dim/CellLoc;)Lthaumcraft/common/lib/world/dim/Cell;"))
    private Cell modifyCell(Cell c, @Local(name = "cx") int cx, @Local(name = "cz") int cz, @Local(name = "a") int a,
        @Local(name = "b") int b) {
        Maze maze = Maze.getMazeAtPoint(cx + a, cz + b);
        int room = maze.getRoom(a, b);
        return new Cell((short) room);
    }
}
