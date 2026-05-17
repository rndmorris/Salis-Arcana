/*
 * Copyright (c) 2020 Jonathan Simmons
 * Licensed under the MIT License
 * Taken from BugTorch
 * https://github.com/jss2a98aj/BugTorch
 */

package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.blocks.BlockCandle;
import thaumcraft.common.lib.utils.Utils;

@Mixin(BlockCandle.class)
public abstract class MixinBlockCandle_OOB {

    /**
     * @author jss2a98aj
     * @reason Prevents an array out of bounds exception when metadata greater than 15 is used.
     */
    @ModifyVariable(method = "getRenderColor", at = @At("HEAD"), argsOnly = true)
    public int getRenderColorSafety(int meta) {
        return meta >= Utils.colors.length ? 0 : meta;
    }

    /**
     * @author jss2a98aj
     * @reason Prevents an array out of bounds exception when metadata greater than 15 is used.
     */
    @ModifyExpressionValue(
        method = "colorMultiplier",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockMetadata(III)I"))
    public int colorMultiplierSafety(int meta) {
        return meta >= Utils.colors.length ? 0 : meta;
    }
}
