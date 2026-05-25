package dev.rndmorris.salisarcana.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

import com.github.bsideup.jabel.Desugar;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

public class NodeRenderingQueue {

    public static final List<QueuedNode> nodeQueue = new ArrayList<>();
    public static final List<QueuedLightning> lightningQueue = new ArrayList<>();
    public static final List<QueuedDrainBeam> drainQueue = new ArrayList<>();
    public static final List<Runnable> tagQueue = new ArrayList<>();

    @Desugar
    public record QueuedNode(int x, int y, int z, double viewDistance, boolean visible, boolean depthIgnore, float size,
        AspectList aspects, NodeType type, NodeModifier mod) {}

    @Desugar
    public record QueuedLightning(double x, double y, double z, String texture, int frames) {}

    @Desugar
    public record QueuedDrainBeam(double fromX, double fromY, double fromZ, double toX, double toY, double toZ,
        int color, float alpha, float partialTicks) {}

    public static void flush(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase viewer = mc.renderViewEntity;

        if (!nodeQueue.isEmpty()) {
            GL11.glPushMatrix();
            for (QueuedNode qn : nodeQueue) {
                TileNodeRenderer.renderNode(
                    viewer,
                    qn.viewDistance(),
                    qn.visible(),
                    qn.depthIgnore(),
                    qn.size(),
                    qn.x(),
                    qn.y(),
                    qn.z(),
                    partialTicks,
                    qn.aspects(),
                    qn.type(),
                    qn.mod());
            }
            GL11.glPopMatrix();
            nodeQueue.clear();
        }

        if (!lightningQueue.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDepthMask(false);
            long nt = System.nanoTime();
            for (QueuedLightning ql : lightningQueue) {
                UtilsFX.bindTexture(ql.texture());
                int i = (int) (((double) (nt / 40000000L) + ql.x()) % (double) ql.frames());
                GL11.glColor4f(1.0F, 0.0F, 1.0F, 0.9F);
                UtilsFX.renderFacingQuad(
                    ql.x(),
                    ql.y(),
                    ql.z(),
                    0.0F,
                    0.33F,
                    0.9F,
                    ql.frames(),
                    i,
                    partialTicks,
                    16777215);
            }
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
            lightningQueue.clear();
        }

        if (!drainQueue.isEmpty()) {
            GL11.glPushMatrix();
            for (QueuedDrainBeam qdb : drainQueue) {
                UtilsFX.drawFloatyLine(
                    qdb.fromX(),
                    qdb.fromY(),
                    qdb.fromZ(),
                    qdb.toX(),
                    qdb.toY(),
                    qdb.toZ(),
                    qdb.partialTicks(),
                    qdb.color(),
                    "textures/misc/wispy.png",
                    -0.02F,
                    qdb.alpha());
            }
            GL11.glPopMatrix();
            drainQueue.clear();
        }

        if (!tagQueue.isEmpty()) {
            for (Runnable callback : tagQueue) {
                callback.run();
            }
            tagQueue.clear();
        }
    }
}
