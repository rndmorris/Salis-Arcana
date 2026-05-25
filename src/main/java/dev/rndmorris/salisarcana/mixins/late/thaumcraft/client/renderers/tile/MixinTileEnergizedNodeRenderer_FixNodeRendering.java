package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.tile;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.rndmorris.salisarcana.lib.NodeRenderingQueue;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.renderers.tile.TileNodeEnergizedRenderer;

@Mixin(value = TileNodeEnergizedRenderer.class, remap = false)
public class MixinTileEnergizedNodeRenderer_FixNodeRendering {

    @Shadow
    static String tx1;

    @Redirect(
        method = "renderTileEntityAt",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"))
    private void redirectRenderNode(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore,
        float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        NodeRenderingQueue.nodeQueue.add(
            new NodeRenderingQueue.QueuedNode(x, y, z, viewDistance, visible, depthIgnore, size, aspects, type, mod));
    }

    @Redirect(
        method = "renderTileEntityAt",
        at = @At(value = "INVOKE", target = "Lthaumcraft/client/lib/UtilsFX;renderFacingQuad(DDDFFFIIFI)V"))
    private void redirectLightningQuad(double x, double y, double z, float angle, float scale, float alpha, int frames,
        int frame, float partialTicks, int color) {
        NodeRenderingQueue.lightningQueue.add(new NodeRenderingQueue.QueuedLightning(x, y, z, tx1, frames));
    }
}
