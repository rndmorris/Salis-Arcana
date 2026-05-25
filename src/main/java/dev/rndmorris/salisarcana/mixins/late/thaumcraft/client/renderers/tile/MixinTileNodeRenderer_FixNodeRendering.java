package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.tile;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.rndmorris.salisarcana.lib.NodeRenderingQueue;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

@Mixin(value = TileNodeRenderer.class, remap = false)
public class MixinTileNodeRenderer_FixNodeRendering {

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
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;drawFloatyLine(DDDDDDFILjava/lang/String;FF)V"))
    private void redirectDrainBeam(double fromX, double fromY, double fromZ, double toX, double toY, double toZ,
        float partialTicks, int color, String texture, float offset, float alpha) {
        NodeRenderingQueue.drainQueue.add(
            new NodeRenderingQueue.QueuedDrainBeam(fromX, fromY, fromZ, toX, toY, toZ, color, alpha, partialTicks));
    }
}
