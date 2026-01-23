package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import baubles.api.BaublesApi;
import thaumcraft.common.lib.events.EventHandlerRunic;

@Mixin(value = EventHandlerRunic.class, remap = false)
public abstract class MixinEventHandlerRunic_ExtendedBaublesSupport {

    @ModifyConstant(method = "livingTick", constant = @Constant(intValue = 4, ordinal = 1), remap = false)
    private int useAllBaubleSlots(int value, LivingEvent.LivingUpdateEvent event) {
        return BaublesApi.getBaubles((EntityPlayer) event.entity)
            .getSizeInventory();
    }

}
