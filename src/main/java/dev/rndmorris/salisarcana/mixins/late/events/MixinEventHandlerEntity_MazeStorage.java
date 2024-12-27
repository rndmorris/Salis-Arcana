package dev.rndmorris.salisarcana.mixins.late.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.Maze;
import thaumcraft.common.lib.events.EventHandlerEntity;
import thaumcraft.common.lib.world.dim.Cell;

@Mixin(value = EventHandlerEntity.class, remap = false)
public class MixinEventHandlerEntity_MazeStorage {

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
        method = "isDangerousLocation",
        at = @org.spongepowered.asm.mixin.injection.At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/world/dim/MazeHandler;getFromHashMap(Lthaumcraft/common/lib/world/dim/CellLoc;)Lthaumcraft/common/lib/world/dim/Cell;"))
    private Cell modifyCell(Cell orig, @Local(name = "xx") int xx, @Local(name = "zz") int zz) {
        Maze maze = Maze.getMazeAtPoint(xx, zz);
        int room = maze.getRoom(xx, zz);
        return new Cell((short) room);
    }
}
