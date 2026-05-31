package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.fx;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import thaumcraft.client.fx.ParticleEngine;

@Mixin(value = ParticleEngine.class, remap = false)
abstract class MixinParticleEngine_PauseParticles {

    @Inject(method = "updateParticles", at = @At("HEAD"), cancellable = true)
    private void onUpdateParticles(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if (FMLClientHandler.instance()
            .getClient()
            .isGamePaused()) {
            ci.cancel();
        }
    }

}
