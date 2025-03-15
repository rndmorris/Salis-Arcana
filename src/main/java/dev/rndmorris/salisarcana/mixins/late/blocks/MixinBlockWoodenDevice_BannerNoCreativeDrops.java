package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin(BlockWoodenDevice.class)
public class MixinBlockWoodenDevice_BannerNoCreativeDrops {

    @WrapWithCondition(
        method = "onBlockHarvested",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/blocks/BlockWoodenDevice;dropBlockAsItem(Lnet/minecraft/world/World;IIIII)V"))
    public boolean shouldDropBanner(BlockWoodenDevice instance, World world, int i, int j, int k, int l, int m,
        @Local(argsOnly = true) EntityPlayer player) {
        return !player.capabilities.isCreativeMode;
    }
}
