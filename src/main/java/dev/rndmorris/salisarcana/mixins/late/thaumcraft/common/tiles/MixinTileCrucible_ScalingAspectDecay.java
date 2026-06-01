package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class, remap = false)
abstract class MixinTileCrucible_ScalingAspectDecay extends TileThaumcraft {

    @Shadow
    public short heat;
    @Shadow
    public AspectList aspects;
    @Shadow
    private long counter;

    @Shadow
    public abstract int tagAmount();

    @Shadow
    public abstract void spill();

    @Inject(method = "updateEntity", at = @At("TAIL"), remap = true)
    private void additionalScaledAspectDrain(CallbackInfo ci) {
        if (worldObj.isRemote || heat <= 150 || ((int) counter + 1) % 20 != 0) return;

        int total = tagAmount();
        int decayStart = SalisConfig.thaum.crucibleAspectDecayStart.getValue();
        float maxRate = SalisConfig.thaum.crucibleAspectDecayMaximumRate.getValue() / 100f;

        int excess = total - decayStart;
        if (excess <= 0) return;

        double percentage = Math.min(
            (double) total / (decayStart + SalisConfig.thaum.crucibleAspectDecayRange.getValue()) * maxRate,
            maxRate);
        int removeCount = Math.max(0, (int) Math.ceil((total * percentage)));

        salisarcana$removeAndSplit(removeCount);

        int spills = Math.min(1 + (removeCount / 10), 5);
        for (int i = 0; i < spills; i++) {
            spill();
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Unique
    private void salisarcana$removeAndSplit(int removeCount) {
        if (aspects.size() == 0 || removeCount <= 1) return;
        Random rand = worldObj.rand;

        int splitRemoval = (int) Math.ceil((double) removeCount / aspects.size());

        int toRemove;
        int remainder = 0;
        int totalRemoved = 0;
        final var newAspects = new HashMap<>(aspects.aspects);
        final var availableAspects = new ArrayList<>(newAspects.keySet());

        while (totalRemoved < removeCount) {
            Aspect a = availableAspects.get(rand.nextInt(availableAspects.size()));
            if (a.isPrimal()) {
                a = availableAspects.get(rand.nextInt(availableAspects.size()));
            }

            toRemove = Math.min(splitRemoval + remainder, removeCount - totalRemoved);

            int aspectAmount = newAspects.get(a);
            remainder = Math.max(0, toRemove - aspectAmount);
            toRemove = Math.min(toRemove, aspectAmount);

            int newAmount = aspectAmount - toRemove;
            if (newAmount <= 0) {
                newAspects.remove(a);
                availableAspects.remove(a);
            } else {
                newAspects.put(a, newAmount);
            }

            if (!a.isPrimal()) {
                Aspect component = rand.nextBoolean() ? a.getComponents()[0] : a.getComponents()[1];
                newAspects.merge(component, toRemove, Integer::sum);
                if (!availableAspects.contains(component)) {
                    availableAspects.add(component);
                }
            }

            totalRemoved += toRemove;
        }

        aspects.aspects.clear();
        aspects.aspects.putAll(newAspects);
    }
}
