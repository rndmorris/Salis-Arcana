package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import java.util.Map;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.client.lib.UtilsFX;

@Mixin(value = UtilsFX.class, remap = false)
abstract class MixinUtilsFX_BindTextureCache {

    @Shadow(remap = false)
    static Map<String, ResourceLocation> boundTextures;

    @ModifyExpressionValue(
        method = "bindTexture(Ljava/lang/String;)V",
        at = @At(value = "NEW", target = "net/minecraft/util/ResourceLocation", remap = true))
    private static ResourceLocation salisarcana$cacheTexture(ResourceLocation original, String texture) {
        boundTextures.put(texture, original);
        return original;
    }

    @ModifyExpressionValue(
        method = "bindTexture(Ljava/lang/String;Ljava/lang/String;)V",
        at = @At(value = "NEW", target = "net/minecraft/util/ResourceLocation", remap = true))
    private static ResourceLocation salisarcana$cacheTexture(ResourceLocation original, String mod, String texture) {
        boundTextures.put(mod + ":" + texture, original);
        return original;
    }

}
