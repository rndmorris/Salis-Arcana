package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.fx;

import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import cpw.mods.fml.common.gameevent.TickEvent;
import thaumcraft.client.fx.ParticleEngine;

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine_SkipRendering {

    @Unique
    private boolean sa$hasParticles;

    @WrapMethod(method = "onRenderWorldLast")
    private void sa$onRenderWorldLast(RenderWorldLastEvent event, Operation<Void> original) {
        if (sa$hasParticles) {
            original.call(event);
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

    @WrapMethod(method = "updateParticles")
    private void sa$onUpdateParticles(TickEvent.ClientTickEvent event, Operation<Void> original) {
        if (sa$hasParticles) {
            original.call(event);
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
