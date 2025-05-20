package dev.rndmorris.salisarcana.mixins.late.client.renderers;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;

import thaumcraft.client.renderers.tile.TileEldritchObeliskRenderer;

@Mixin(TileEldritchObeliskRenderer.class)
public class MixinTileEldritchObeliskRenderer_FixFog {

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"))
    public void checkIfFogApplies(TileEntity te, double x, double y, double z, float f, CallbackInfo ci,
        @Share("fogEnabled") LocalBooleanRef fogEnabled) {
        fogEnabled.set(GL11.glIsEnabled(GL11.GL_FOG));
    }

    @WrapOperation(
        method = "renderTileEntityAt",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", remap = false, ordinal = 0))
    public void cancelFog(int cap, Operation<Void> original, @Share("fogEnabled") LocalBooleanRef fogEnabled) {
        if (fogEnabled.get()) {
            original.call(cap);
        }
    }
}
