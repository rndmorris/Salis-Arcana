package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.lib.NodeRenderingQueue;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
abstract class MixinRenderEventHandler_FixNodeRendering {

    @Redirect(
        method = "blockHighlight",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/RenderEventHandler;drawTagsOnContainer(DDDLthaumcraft/api/aspects/AspectList;ILnet/minecraftforge/common/util/ForgeDirection;F)V"))
    private void redirectDrawTags(RenderEventHandler instance, double x, double y, double z, AspectList aspects,
        int bright, ForgeDirection dir, float partialTicks) {
        NodeRenderingQueue.tagQueue
            .add(() -> instance.drawTagsOnContainer(x, y, z, aspects, bright, dir, partialTicks));
    }

    @Inject(method = "renderLast", at = @At("TAIL"))
    private void onRenderLast(RenderWorldLastEvent event, CallbackInfo ci) {
        NodeRenderingQueue.flush(event.partialTicks);
    }
}
