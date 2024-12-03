package dev.rndmorris.tfixins.mixins.late.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.entities.monster.EntityEldritchCrab;

@Mixin(value = EntityEldritchCrab.class, remap = false)
public abstract class MixinEntityEldritchCrab extends EntityMob {

    public MixinEntityEldritchCrab(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @Inject(method = "attackEntityAsMob", at = @At("HEAD"), cancellable = true, remap = false)
    public void onAttackEntityAsMob(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (!this.isEntityAlive()) {
            ci.setReturnValue(false);
            ci.cancel();
        }
    }
}
