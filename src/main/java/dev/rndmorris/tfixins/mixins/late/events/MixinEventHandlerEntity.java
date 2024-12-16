package dev.rndmorris.tfixins.mixins.late.events;

import static dev.rndmorris.tfixins.ThaumicFixins.LOG;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(value = EventHandlerEntity.class, remap = false)
public abstract class MixinEventHandlerEntity {

    @WrapOperation(
        method = "livingTick(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingUpdateEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/WarpEvents;checkWarpEvent(Lnet/minecraft/entity/player/EntityPlayer;)V"))
    private void suppressCreativeWarpEvent(EntityPlayer player, Operation<EntityPlayer> original) {
        if (player.capabilities.isCreativeMode) {
            LOG.info("Canceled a creative warp event!");
            return;
        }
        original.call(player);
    }

}
