package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin(BlockWoodenDevice.class)
public class MixinBlockWoodenDevice_NoBannerCreativeDrops {

    @WrapWithCondition(
        method = "onBlockHarvested",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/blocks/BlockWoodenDevice;dropBlockAsItem(Lnet/minecraft/world/World;IIIII)V"))
    public boolean requireSurvival(BlockWoodenDevice instance, World world, int x, int y, int z, int meta, int fortune,
        @Local(argsOnly = true) EntityPlayer player) {
        return !player.capabilities.isCreativeMode;
    }
}
