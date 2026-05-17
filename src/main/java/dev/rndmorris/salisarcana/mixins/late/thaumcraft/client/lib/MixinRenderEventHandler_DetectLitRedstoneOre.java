package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.client.lib.RenderEventHandler;

// this priority positions it to be executed before the big mixin
@Mixin(value = RenderEventHandler.class, remap = false, priority = 1001)
public class MixinRenderEventHandler_DetectLitRedstoneOre {

    @ModifyExpressionValue(
        method = "startScan",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;",
            remap = true))
    private Block substituteLitRedstoneOre(Block original) {
        // lit redstone ore (apparently) does not have an item form, which prevents it from being given oredict labels
        // (or at least that's what the OreDictionary says when I try to register a label for it)
        // that causes problems, so just treat it as normal redstone ore
        return original == Blocks.lit_redstone_ore ? Blocks.redstone_ore : original;
    }
}
