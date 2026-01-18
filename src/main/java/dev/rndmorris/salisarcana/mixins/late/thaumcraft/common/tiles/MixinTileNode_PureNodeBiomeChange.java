package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

@Mixin(TileNode.class)
public class MixinTileNode_PureNodeBiomeChange {

    @ModifyExpressionValue(
        method = "handlePureNode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;"))
    private Block forceTrue(Block original) {
        return ConfigBlocks.blockMagicalLog;
    }
}
