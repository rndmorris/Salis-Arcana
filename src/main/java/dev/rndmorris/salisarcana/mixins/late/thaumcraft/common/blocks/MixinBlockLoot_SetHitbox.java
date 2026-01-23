package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockLoot;

@Mixin(BlockLoot.class)
public class MixinBlockLoot_SetHitbox {

    @WrapOperation(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/blocks/BlockLoot;setBlockBounds(FFFFFF)V"))
    public void setBlockBounds(BlockLoot instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, Operation<Void> original, @Local(argsOnly = true) int renderType) {
        if (renderType == 1) {
            original.call(
                instance,
                BlockRenderer.W2,
                BlockRenderer.W1,
                BlockRenderer.W2,
                BlockRenderer.W14,
                BlockRenderer.W13,
                BlockRenderer.W14);
        } else {
            original.call(
                instance,
                BlockRenderer.W1,
                0.0F,
                BlockRenderer.W1,
                BlockRenderer.W15,
                BlockRenderer.W14,
                BlockRenderer.W15);
        }
    }

    @WrapOperation(
        method = "getSelectedBoundingBoxFromPool",
        at = @At(value = "INVOKE", target = "Lthaumcraft/common/blocks/BlockLoot;setBlockBounds(FFFFFF)V"))
    public void ignoreFurtherChanges(BlockLoot instance, float minX, float minY, float minZ, float maxX, float maxY,
        float maxZ, Operation<Void> original) {
        // The block bounds are being set in the constructor, and they should never need to change again.
        // This injection technically isn't necessary, but it does remove extra useless operations & makes any related
        // issues a lot more obvious.
    }
}
