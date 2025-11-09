package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.Random;

import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileEldritchAltar;

@Mixin(value = TileEldritchAltar.class, remap = false)
public abstract class MixinTileEldritchAltar_SpawnMobs extends TileThaumcraft {

    @WrapOperation(
        method = { "spawnGuards", "spawnGuardian" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/MathHelper;getRandomIntegerInRange(Ljava/util/Random;II)I"))
    private int preventRandCalls(Random random, int min, int max, Operation<Integer> original) {
        return 0;
    }

    @WrapOperation(
        method = { "spawnGuards", "spawnGuardian" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;doesBlockHaveSolidTopSurface(Lnet/minecraft/world/IBlockAccess;III)Z"))
    @SuppressWarnings("ParameterCanBeLocal")
    private boolean pickAndCheckCoords(IBlockAccess worldIn, int x, int y, int z, Operation<Boolean> original,
        @Local(name = "i1") LocalIntRef xRef, @Local(name = "j1") LocalIntRef yRef,
        @Local(name = "k1") LocalIntRef zRef) {

        final var spawnSettings = SalisConfig.features.eldritchAltarSpawningMethod;

        xRef.set(x = xCoord + spawnSettings.randomHorizontal(worldObj.rand));
        yRef.set(y = yCoord + spawnSettings.randomVertical(worldObj.rand));
        zRef.set(z = zCoord + spawnSettings.randomHorizontal(worldObj.rand));

        return original.call(worldIn, x, y - 1, z);
    }
}
