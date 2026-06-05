package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.renderers.tile;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.client.lib.NodeRenderingQueue;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

@Mixin(value = TileNodeRenderer.class)
abstract class MixinTileNodeRenderer_FixNodeRendering {

    @WrapOperation(
        method = "renderTileEntityAt",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"))
    private void wrapRenderNode(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore,
        float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod,
        Operation<Void> original) {
        NodeRenderingQueue.nodeQueue.add(
            new NodeRenderingQueue.QueuedNode(x, y, z, viewDistance, visible, depthIgnore, size, aspects, type, mod));
    }

    @WrapOperation(
        method = "renderTileEntityAt",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/client/lib/UtilsFX;drawFloatyLine(DDDDDDFILjava/lang/String;FF)V"))
    private void wrapDrainBeam(double fromX, double fromY, double fromZ, double toX, double toY, double toZ,
        float partialTicks, int color, String texture, float offset, float alpha, Operation<Void> original) {
        NodeRenderingQueue.drainQueue.add(
            new NodeRenderingQueue.QueuedDrainBeam(fromX, fromY, fromZ, toX, toY, toZ, color, alpha, partialTicks));
    }
}
