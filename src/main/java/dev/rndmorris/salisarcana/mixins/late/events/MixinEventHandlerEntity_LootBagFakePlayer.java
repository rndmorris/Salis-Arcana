package dev.rndmorris.salisarcana.mixins.late.events;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(value = EventHandlerEntity.class, remap = false)
public abstract class MixinEventHandlerEntity_LootBagFakePlayer {

    @Definition(id = "FakePlayer", type = FakePlayer.class, remap = true)
    @Definition(id = "event", local = @Local(name = "event", type = LivingDropsEvent.class))
    @Definition(
        id = "source",
        field = "Lnet/minecraftforge/event/entity/living/LivingDropsEvent;source:Lnet/minecraft/util/DamageSource;")
    @Definition(id = "getEntity", method = "Lnet/minecraft/util/DamageSource;getEntity()Lnet/minecraft/entity/Entity;")
    @Expression("event.source.getEntity() instanceof FakePlayer")
    @ModifyExpressionValue(method = "livingDrops", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean fakePlayersDropLootBags(boolean original) {
        return false;
    }
}
