package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileNode;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode_UnstableAspectDropsChunkGuard extends TileThaumcraft {

    @WrapOperation(
        method = "handleNodeStability",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileNode;takeRandomPrimalFromSource()Lthaumcraft/api/aspects/Aspect;"))
    private Aspect skipUnstableDropsWhenNearbyChunksAreUnloaded(TileNode instance, Operation<Aspect> original) {
        if (!worldObj.checkChunksExist(xCoord - 32, 0, zCoord - 32, xCoord + 32, 0, zCoord + 32)) {
            return null;
        }
        return original.call(instance);
    }
}
