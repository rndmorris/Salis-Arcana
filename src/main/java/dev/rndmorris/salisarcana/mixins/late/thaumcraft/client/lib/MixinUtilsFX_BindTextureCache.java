package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.client.lib.UtilsFX;

@Mixin(value = UtilsFX.class, remap = false)
abstract class MixinUtilsFX_BindTextureCache {

    @Shadow(remap = false)
    private static Map<String, ResourceLocation> boundTextures;

    @Inject(method = "bindTexture(Ljava/lang/String;)V", at = @At("HEAD"))
    private static void salisarcana$cacheTexture(String texture, CallbackInfo ci) {
        boundTextures.computeIfAbsent(texture, key -> new ResourceLocation("thaumcraft", key));
    }

    @Inject(method = "bindTexture(Ljava/lang/String;Ljava/lang/String;)V", at = @At("HEAD"))
    private static void salisarcana$cacheTexture(String mod, String texture, CallbackInfo ci) {
        final var key = mod + ":" + texture;
        if (!boundTextures.containsKey(key)) {
            boundTextures.put(key, new ResourceLocation(mod, texture));
        }
    }

}
