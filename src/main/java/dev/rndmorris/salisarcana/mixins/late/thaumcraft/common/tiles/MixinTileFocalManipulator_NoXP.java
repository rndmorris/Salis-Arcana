package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(TileFocalManipulator.class)
public class MixinTileFocalManipulator_NoXP {

    @ModifyExpressionValue(
        method = "startCraft",
        at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;experienceLevel:I"))
    public int creativeXP(final int original, final int id, final EntityPlayer player) {
        return player.capabilities.isCreativeMode ? Integer.MAX_VALUE : original;
    }
}
