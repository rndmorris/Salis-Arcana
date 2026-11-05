package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.potions;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.EventUtils;
import thaumcraft.common.lib.potions.PotionThaumarhia;

@Mixin(PotionThaumarhia.class)
abstract class MixinPotionThaumarhia_EmitEvents {

    @WrapOperation(
        method = "performEffect",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;)Z"))
    private boolean checkedSetBlock(World instance, int x, int y, int z, Block blockType, Operation<Boolean> original,
        EntityLivingBase target) {
        if (target instanceof EntityPlayer player) {
            return EventUtils.tryPlaceBlock(instance, x, y, z, blockType, 0, 3, player);
        } else {
            return original.call(instance, x, y, z, blockType);
        }
    }
}
