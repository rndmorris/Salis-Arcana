package dev.rndmorris.salisarcana.mixins.late.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.entities.monster.EntityTaintacle;

@Mixin(value = EntityTaintacle.class)
public abstract class MixinEntityTaintacle extends EntityMob {

    public MixinEntityTaintacle(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    public void onAttackEntity(Entity entity, float distance, CallbackInfo ci) {
        if (!this.isEntityAlive()) {
            ci.cancel();
        }
    }

}
