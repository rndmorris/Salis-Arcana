package dev.rndmorris.salisarcana.mixins.late.client.lib;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public abstract class MixinRenderEventHandler_DetectLapisOre {

    @ModifyExpressionValue(
        method = "startScan",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getDamageValue(Lnet/minecraft/world/World;III)I"))
    private int useMetadataInsteadOfDamage(int original, @Local(name = "player") Entity player,
        @Local(name = "x") int x, @Local(name = "y") int y, @Local(name = "z") int z, @Local(name = "xx") int xx,
        @Local(name = "yy") int yy, @Local(name = "zz") int zz) {
        return player.worldObj.getBlockMetadata(x + xx, y + yy, z + zz);
    }
}
