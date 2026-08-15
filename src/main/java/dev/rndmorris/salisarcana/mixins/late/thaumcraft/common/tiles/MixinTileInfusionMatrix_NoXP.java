package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(TileInfusionMatrix.class)
abstract class MixinTileInfusionMatrix_NoXP {

    @ModifyExpressionValue(
        method = "craftCycle",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/entity/player/EntityPlayer;experienceLevel:I"))
    private int creativeXP(int original, @Local EntityPlayer player) {
        return player.capabilities.isCreativeMode ? Integer.MAX_VALUE : original;
    }

    @WrapWithCondition(
        method = "craftCycle",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addExperienceLevel(I)V"))
    private boolean skipCreativeXPDrain(EntityPlayer player, int levels) {
        return !player.capabilities.isCreativeMode;
    }
}
