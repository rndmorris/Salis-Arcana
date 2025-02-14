package dev.rndmorris.salisarcana.mixins.late.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.entities.monster.EntityTaintacle;

@Mixin(value = EntityTaintacle.class)
public abstract class MixinEntityTaintacle extends EntityMob {

    public MixinEntityTaintacle(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @WrapMethod(method = "attackEntity")
    private void onAttackEntity(Entity entity, float distance, Operation<Void> original) {
        if (!this.isEntityAlive()) {
            return;
        }
        original.call(entity, distance);
    }

}
