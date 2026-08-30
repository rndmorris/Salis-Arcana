package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.golems;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import thaumcraft.common.entities.golems.EntityTravelingTrunk;

@Mixin(EntityTravelingTrunk.class)
abstract class MixinEntityTravelingTrunk_SkipDeadItems {

    @Redirect(
        method = "pullItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"))
    private <T> List<T> skipDeadItems(World instance, Class<T> entityClass, AxisAlignedBB axisAlignedBB) {
        // `IEntitySelector.selectAnything` skips all dead entities.
        return instance.selectEntitiesWithinAABB(entityClass, axisAlignedBB, IEntitySelector.selectAnything);
    }
}
