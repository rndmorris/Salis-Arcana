package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import java.util.HashSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.lib.EntityHelper;
import thaumcraft.common.lib.events.EventHandlerEntity;

@Mixin(value = EventHandlerEntity.class, remap = false)
public class MixinEventHandlerEntity_MobVisWhitelist {

    @Unique
    private final boolean sa$isWhitelist = SalisConfig.features.mobVisWhitelist.isEnabled();

    @Unique
    private HashSet<Class<? extends Entity>> sa$entities = null;

    // we need this for both nonstatic fields to be initialized in the real constructor
    public MixinEventHandlerEntity_MobVisWhitelist() {

    }

    @WrapOperation(
        method = "livingTick(Lnet/minecraftforge/event/entity/living/LivingDeathEvent;)V",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/utils/EntityUtils;getRecentlyHit(Lnet/minecraft/entity/EntityLivingBase;)I"))
    private int sa$shouldGenerateVisOrbs(EntityLivingBase e, Operation<Integer> original) {
        if (sa$isWhitelist) {
            if (sa$getEntities().contains(e.getClass())) {
                return original.call(e);
            }
        } else {
            if (!sa$getEntities().contains(e.getClass())) {
                return original.call(e);
            }
        }
        return 0;
    }

    @Unique
    private HashSet<Class<? extends Entity>> sa$getEntities() {
        return sa$entities != null ? sa$entities
            : (sa$entities = EntityHelper.getEntitiesFromStringArr(SalisConfig.features.mobVisDropList.getValue()));
    }
}
