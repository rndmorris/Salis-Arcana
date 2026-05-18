package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(TileInfusionMatrix.class)
public class MixinTileInfusionMatrix_NoXP {

    @WrapOperation(
        method = "craftCycle",
        at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;experienceLevel:I"))
    private int creativeXP(EntityPlayer player, Operation<Integer> original) {
        return player.capabilities.isCreativeMode ? Integer.MAX_VALUE : original.call(player);
    }

    @WrapOperation(
        method = "craftCycle",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addExperienceLevel(I)V"))
    private void skipCreativeXPDrain(EntityPlayer player, int levels, Operation<Void> original) {
        if (!player.capabilities.isCreativeMode) {
            original.call(player, levels);
        }
    }
}
