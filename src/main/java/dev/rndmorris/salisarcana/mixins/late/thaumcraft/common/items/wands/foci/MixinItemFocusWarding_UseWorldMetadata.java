package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.api.BlockCoordinates;
import thaumcraft.common.items.wands.foci.ItemFocusWarding;

@Mixin(ItemFocusWarding.class)
abstract class MixinItemFocusWarding_UseWorldMetadata {

    @ModifyArg(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 1,
            remap = true),
        index = 4,
        remap = false)
    private int getMetadataFromWorld(int metadata, @Local(argsOnly = true) World world, @Local BlockCoordinates c) {
        return world.getBlockMetadata(c.x, c.y, c.z);
    }
}
