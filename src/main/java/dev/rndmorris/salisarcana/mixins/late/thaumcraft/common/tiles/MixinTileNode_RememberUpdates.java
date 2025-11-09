package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import dev.rndmorris.salisarcana.lib.ifaces.IGameTimeNode;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class)
public abstract class MixinTileNode_RememberUpdates extends TileThaumcraft {

    @Shadow(remap = false)
    long lastActive;

    /**
     * When the node is drained in some way, log that the node was active
     */
    @WrapMethod(method = "takeFromContainer(Lthaumcraft/api/aspects/Aspect;I)Z", remap = false)
    private boolean updateLastActiveOnTake(Aspect aspect, int amount, Operation<Boolean> original) {
        this.lastActive = System.currentTimeMillis();
        if (this instanceof IGameTimeNode node) {
            node.sa$updateLastTickActive();
        }
        return original.call(aspect, amount);
    }

    /**
     * When vis is added to the node, log that it was active.
     */
    @WrapMethod(method = "addToContainer", remap = false)
    private int updateLastActiveOnAdd(Aspect aspect, int amount, Operation<Integer> original) {
        this.lastActive = System.currentTimeMillis();
        if (this instanceof IGameTimeNode node) {
            node.sa$updateLastTickActive();
        }
        return original.call(aspect, amount);
    }

}
