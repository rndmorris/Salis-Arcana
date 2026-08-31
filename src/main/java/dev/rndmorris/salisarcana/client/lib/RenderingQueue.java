package dev.rndmorris.salisarcana.client.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.github.bsideup.jabel.Desugar;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.RenderEventHandler;

public class RenderingQueue {

    private static final List<QueuedTag> tagQueue = new ArrayList<>();

    @Desugar
    private record QueuedTag(RenderEventHandler instance, double x, double y, double z, AspectList aspects, int bright,
        ForgeDirection dir, float partialTicks, Operation<Void> original) {}

    public static void flush() {
        for (QueuedTag qt : tagQueue) {
            qt.original()
                .call(qt.instance(), qt.x(), qt.y(), qt.z(), qt.aspects(), qt.bright(), qt.dir(), qt.partialTicks());
        }
        tagQueue.clear();
    }

    public static void queueTag(RenderEventHandler instance, double x, double y, double z, AspectList aspects,
        int bright, ForgeDirection dir, float partialTicks, Operation<Void> original) {
        tagQueue.add(new QueuedTag(instance, x, y, z, aspects, bright, dir, partialTicks, original));
    }

}
