package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.tiles.TileFocalManipulator;

@Mixin(TileFocalManipulator.class)
public class MixinTileFocalManipulator_ForbidSwap {

    @Shadow(remap = false)
    public int size;

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSoundEffect(DDDLjava/lang/String;FF)V",
            ordinal = 0))
    public void cancelCraft(CallbackInfo ci) {
        this.size = 0;
    }
}
