package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.entities.EntityAspectOrb;

@Mixin(EntityAspectOrb.class)
abstract class MixinEntityAspectOrb_ChunkGuard extends Entity {

    public MixinEntityAspectOrb_ChunkGuard(World world) {
        super(world);
    }

    @Inject(method = "onUpdate", at = @At(value = "TAIL"))
    private void setDeadWhenMovedIntoUnloadedChunk(CallbackInfo ci) {
        if (worldObj.isRemote) {
            return;
        }

        int x = MathHelper.floor_double(posX);
        int z = MathHelper.floor_double(posZ);
        if (!worldObj.checkChunksExist(x - 32, 0, z - 32, x + 32, 0, z + 32)) {
            setDead();
        }
    }
}
