package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(value = EventHandlerEntity.class, remap = false, priority = 1001)
public abstract class MixinEventHandlerEntity_LootBagFakePlayer {

    @ModifyVariable(method = "livingDrops", at = @At("STORE"), name = "fakeplayer")
    private static boolean fakePlayersDropLootBags(boolean original) {
        return false;
    }
}
