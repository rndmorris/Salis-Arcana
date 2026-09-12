package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.entities.projectile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.EventUtils;
import thaumcraft.common.entities.projectile.EntityShockOrb;

@Mixin(EntityShockOrb.class)
abstract class MixinEntityShockOrb_EmitPlaceEvent extends EntityThrowable {

    public MixinEntityShockOrb_EmitPlaceEvent(World p_i1776_1_) {
        super(p_i1776_1_);
    }

    @WrapOperation(
        method = "onImpact",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z"))
    private boolean checkPlaceEvent(World world, int x, int y, int z, Block block, int metadata, int flags,
        Operation<Boolean> original) {
        if (this.getThrower() instanceof EntityPlayer player) {
            return EventUtils.tryPlaceBlock(world, x, y, z, block, metadata, flags, player);
        } else {
            return original.call(world, x, y, z, block, metadata, flags);
        }
    }
}
