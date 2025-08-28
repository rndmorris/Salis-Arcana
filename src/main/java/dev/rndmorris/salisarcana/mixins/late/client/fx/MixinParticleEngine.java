package dev.rndmorris.salisarcana.mixins.late.client.fx;

import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.client.fx.ParticleEngine;

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine {

    @Unique
    private boolean sa$hasParticles;

    @Inject(method = "onRenderWorldLast", at = @At("HEAD"), cancellable = true)
    private void sa$onRenderWorldLast(RenderWorldLastEvent event, CallbackInfo ci) {
        if (!sa$hasParticles) {
            ci.cancel();
        }
    }

    @Redirect(
        method = "onRenderWorldLast",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V"))
    private void sa$removeUselessGlPush() {}

    @Redirect(
        method = "onRenderWorldLast",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V"))
    private void sa$removeUselessGlPop() {}

    @Inject(method = "addEffect", at = @At("TAIL"))
    private void sa$addEffect(CallbackInfo ci) {
        sa$hasParticles = true;
    }

    @Inject(method = "updateParticles", at = @At("HEAD"), cancellable = true)
    private void sa$shortCircuitUpdate(CallbackInfo ci) {
        if (!sa$hasParticles) {
            ci.cancel();
        }
    }

    @Inject(method = "updateParticles", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 0))
    private void sa$beforeForLoop(CallbackInfo ci) {
        sa$hasParticles = false;
    }

    @Inject(
        method = "updateParticles",
        at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;get(I)Ljava/lang/Object;"))
    private void sa$updateHasParticles(CallbackInfo ci) {
        sa$hasParticles = true;
    }
}
