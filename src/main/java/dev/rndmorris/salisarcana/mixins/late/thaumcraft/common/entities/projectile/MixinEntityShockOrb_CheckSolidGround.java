package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.projectile;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.entities.projectile.EntityShockOrb;

@Mixin(EntityShockOrb.class)
public class MixinEntityShockOrb_CheckSolidGround {

    @WrapOperation(
        method = "onImpact",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(III)Z", ordinal = 2))
    public boolean isSolidBlock(World world, int x, int y, int z, Operation<Boolean> original) {
        return !World.doesBlockHaveSolidTopSurface(world, x, y, z);
    }
}
