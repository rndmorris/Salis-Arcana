package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.DeadlyGazeEntitySelector;
import thaumcraft.common.lib.WarpEvents;

@Mixin(WarpEvents.class)
public class MixinWarpEvents_DeadlyGaze {

    @WrapOperation(
        method = "checkDeathGaze",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
    private static List wrapGetEntities(World instance, Entity entity, AxisAlignedBB box,
        Operation<List<Entity>> original) {
        return instance.getEntitiesWithinAABBExcludingEntity(entity, box, DeadlyGazeEntitySelector.INSTANCE);
    }
}
