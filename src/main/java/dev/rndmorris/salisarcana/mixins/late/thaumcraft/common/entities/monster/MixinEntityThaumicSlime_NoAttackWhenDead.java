package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.entities.monster.EntityThaumicSlime;

@Mixin(value = EntityThaumicSlime.class)
public class MixinEntityThaumicSlime_NoAttackWhenDead extends EntityMob {

    public MixinEntityThaumicSlime_NoAttackWhenDead(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @Inject(method = "onCollideWithPlayer", at = @At("HEAD"), cancellable = true)
    public void onOnCollideWithPlayer(EntityPlayer player, CallbackInfo ci) {
        if (!this.isEntityAlive()) {
            ci.cancel();
        }
    }

    @Redirect(
        method = "updateEntityActionState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
            ordinal = 0))
    public boolean proxySpawnEntityInWorld(World target, Entity entity) {
        if (!this.isEntityAlive()) {
            return false;
        }
        return worldObj.spawnEntityInWorld(entity);
    }

}
