package dev.rndmorris.salisarcana.mixins.late.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.entities.monster.EntityEldritchCrab;

@Mixin(value = EntityEldritchCrab.class)
public abstract class MixinEntityEldritchCrab extends EntityMob {

    public MixinEntityEldritchCrab(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @WrapMethod(method = "attackEntityAsMob")
    private boolean onAttackEntityAsMob(Entity entity, Operation<Boolean> original) {
        if (!this.isEntityAlive()) {
            return false;
        }
        return original.call(entity);
    }
}
