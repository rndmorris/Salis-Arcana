package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public abstract class MixinRenderEventHandler_DetectLapisOre {

    @WrapOperation(
        method = "startScan",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getDamageValue(Lnet/minecraft/world/World;III)I",
            remap = true))
    private int useMetadataInsteadOfDamage(Block instance, World world, int x, int y, int z,
        Operation<Integer> original) {
        return world.getBlockMetadata(x, y, z);
    }
}
