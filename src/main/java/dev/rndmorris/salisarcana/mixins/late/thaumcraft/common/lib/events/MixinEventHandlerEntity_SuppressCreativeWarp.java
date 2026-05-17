package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(value = EventHandlerEntity.class, remap = false)
public abstract class MixinEventHandlerEntity_SuppressCreativeWarp {

    @WrapOperation(
        method = "livingTick(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingUpdateEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/WarpEvents;checkWarpEvent(Lnet/minecraft/entity/player/EntityPlayer;)V"))
    private void suppressCreativeWarpEvent(EntityPlayer player, Operation<EntityPlayer> original) {
        // This check is in the event handler, instead checkWarpEvent itself, to avoid interfering
        // with any mods or addons that want to call that method themselves.
        if (player.capabilities.isCreativeMode) {
            return;
        }
        original.call(player);
    }

}
