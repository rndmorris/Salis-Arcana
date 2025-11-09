package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.tiles.TileEldritchTrap;

@Mixin(TileEldritchTrap.class)
public class MixinTileEldritchTrap_CreativeImmunity {

    @WrapOperation(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getClosestPlayer(DDDD)Lnet/minecraft/entity/player/EntityPlayer;"))
    public EntityPlayer getClosestSurvivalPlayer(World world, double x, double y, double z, double maxDistance,
        Operation<EntityPlayer> original) {
        EntityPlayer closest = null;
        double minDistanceSq = maxDistance * maxDistance;

        for (final EntityPlayer player : world.playerEntities) {
            if (player.capabilities.isCreativeMode || player.capabilities.disableDamage) continue;

            final double distanceSq = player.getDistanceSq(x, y, z);
            if (distanceSq <= minDistanceSq) {
                closest = player;
                minDistanceSq = distanceSq;
            }
        }

        return closest;
    }
}
