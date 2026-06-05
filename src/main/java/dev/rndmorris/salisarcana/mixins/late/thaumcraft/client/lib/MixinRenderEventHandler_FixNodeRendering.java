package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.client.lib.NodeRenderingQueue;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
abstract class MixinRenderEventHandler_FixNodeRendering {

    @WrapOperation(
        method = "blockHighlight",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/RenderEventHandler;drawTagsOnContainer(DDDLthaumcraft/api/aspects/AspectList;ILnet/minecraftforge/common/util/ForgeDirection;F)V"))
    private void wrapDrawTags(RenderEventHandler instance, double x, double y, double z, AspectList aspects, int bright,
        ForgeDirection dir, float partialTicks, Operation<Void> original) {
        NodeRenderingQueue.queueTag(instance, x, y, z, aspects, bright, dir, partialTicks, original);
    }

    @Inject(method = "renderLast", at = @At("TAIL"))
    private void onRenderLast(RenderWorldLastEvent event, CallbackInfo ci) {
        NodeRenderingQueue.flush(event.partialTicks);
    }
}
