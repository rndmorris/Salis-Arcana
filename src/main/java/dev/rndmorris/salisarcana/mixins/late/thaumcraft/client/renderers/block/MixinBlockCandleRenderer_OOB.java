/*
 * Copyright (c) 2020 Jonathan Simmons
 * Licensed under the MIT License
 * Taken from BugTorch
 * https://github.com/jss2a98aj/BugTorch
 */

package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import thaumcraft.client.renderers.block.BlockCandleRenderer;
import thaumcraft.common.lib.utils.Utils;

@Mixin(BlockCandleRenderer.class)
public abstract class MixinBlockCandleRenderer_OOB {

    /**
     * @author jss2a98aj
     * @reason Prevents an array out of bounds exception when metadata greater than 15 is used.
     */
    @ModifyVariable(
        method = "renderInventoryBlock(Lnet/minecraft/block/Block;IILnet/minecraft/client/renderer/RenderBlocks;)V",
        at = @At("HEAD"),
        ordinal = 0,
        remap = false,
        argsOnly = true)
    private int sanitizeRenderInventoryBlock(int meta) {
        return meta >= Utils.colors.length ? 0 : meta;
    }

}
