package dev.rndmorris.salisarcana.mixins.late.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.Maze;
import thaumcraft.common.lib.events.EventHandlerWorld;
import thaumcraft.common.lib.world.dim.Cell;

@Mixin(value = EventHandlerWorld.class, remap = false)
public class MixinEventHandlerWorld {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
        method = "isNearActiveBoss",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/world/dim/MazeHandler;getFromHashMap(Lthaumcraft/common/lib/world/dim/CellLoc;)Lthaumcraft/common/lib/world/dim/Cell;"))
    private Cell modifyCell(Cell orig, @Local(name = "xx") int xx, @Local(name = "zz") int zz) {
        Maze maze = Maze.getMazeAtPoint(xx, zz);
        int room = maze.getRoom(xx, zz);
        return new Cell((short) room);
    }
}
