package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.entities.monster.EntityEldritchCrab;

@Mixin(value = EntityEldritchCrab.class)
public abstract class MixinEntityEldritchCrab_NoAttackWhenDead extends EntityMob {

    public MixinEntityEldritchCrab_NoAttackWhenDead(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @Inject(method = "attackEntityAsMob", at = @At("HEAD"), cancellable = true)
    public void onAttackEntityAsMob(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (!this.isEntityAlive()) {
            ci.setReturnValue(false);
            ci.cancel();
        }
    }
}
