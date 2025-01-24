package dev.rndmorris.salisarcana.mixins.late.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.entities.monster.EntityThaumicSlime;

@Mixin(value = EntityThaumicSlime.class)
public abstract class MixinEntityThaumicSlime extends EntityMob {

    public MixinEntityThaumicSlime(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @WrapMethod(method = "onCollideWithPlayer")
    public void onOnCollideWithPlayer(EntityPlayer player, Operation<Void> original) {
        if (!this.isEntityAlive()) {
            return;
        }
        original.call(player);
    }

    @WrapOperation(
        method = "updateEntityActionState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
            ordinal = 0))
    public boolean proxySpawnEntityInWorld(World target, Entity entity, Operation<Boolean> original) {
        if (!this.isEntityAlive()) {
            return false;
        }
        return original.call(target, entity);
    }

}
