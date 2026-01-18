package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Cancellable;

import thaumcraft.common.blocks.BlockTube;

@Mixin(BlockTube.class)
public abstract class MixinBlockTube_BBoxConserveBlockBounds {

    @Redirect(
        method = "getSelectedBoundingBoxFromPool",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/blocks/BlockTube;setBlockBounds(FFFFFF)V"))
    private void conserveBlockBounds(BlockTube instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, World world, int x, int y, int z, @Cancellable CallbackInfoReturnable<AxisAlignedBB> cir) {
        cir.setReturnValue(AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ));
    }
}
