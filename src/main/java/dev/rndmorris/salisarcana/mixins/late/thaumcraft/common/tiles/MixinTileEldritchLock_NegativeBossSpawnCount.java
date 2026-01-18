package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.world.dim.MapBossData;
import thaumcraft.common.tiles.TileEldritchLock;

@Mixin(TileEldritchLock.class)
public class MixinTileEldritchLock_NegativeBossSpawnCount {

    @WrapOperation(
        method = "doBossSpawn",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/world/dim/MapBossData;markDirty()V", ordinal = 1))
    private void ensureNonNegative(MapBossData instance, Operation<Void> original) {
        instance.bossCount %= 4;
        original.call(instance);
    }
}
