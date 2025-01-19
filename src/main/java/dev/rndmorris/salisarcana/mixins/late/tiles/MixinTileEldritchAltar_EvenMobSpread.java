package dev.rndmorris.salisarcana.mixins.late.tiles;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileEldritchAltar;

@Mixin(value = TileEldritchAltar.class, remap = false)
public abstract class MixinTileEldritchAltar_EvenMobSpread extends TileThaumcraft {

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

        xRef.set(x = xCoord + sa$randomHorizontal());
        yRef.set(y = yCoord + sa$randomVertical());
        zRef.set(z = zCoord + sa$randomHorizontal());

        return original.call(worldIn, x, y - 1, z);
    }

    @Unique
    private int sa$randomHorizontal() {
        return (MathHelper.getRandomIntegerInRange(worldObj.rand, 0, 6) + 4) * (worldObj.rand.nextBoolean() ? 1 : -1);
    }

    @Unique
    private int sa$randomVertical() {
        return MathHelper.getRandomIntegerInRange(worldObj.rand, -3, 3);
    }
}
