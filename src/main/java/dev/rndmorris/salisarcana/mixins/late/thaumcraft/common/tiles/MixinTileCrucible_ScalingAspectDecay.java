package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class, remap = false)
public abstract class MixinTileCrucible_ScalingAspectDecay extends TileThaumcraft {

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

    @Inject(method = "updateEntity", at = @At("TAIL"))
    private void additionalScaledAspectDrain(CallbackInfo ci) {
        if (worldObj.isRemote || heat <= 150 || ((int) counter + 1) % 20 != 0) return;

        int total = tagAmount();
        int excess = total - 200;
        if (excess <= 0) return;

        double percentage = Math.min(total / 1000.00 * 0.042, 0.042);
        int removeCount = Math.max(0, (int) Math.ceil((total * percentage)));

        salis_Arcana$removeAndSplit(removeCount);

        int spills = Math.min(1 + (removeCount / 10), 5);
        for (int i = 0; i < spills; i++) {
            spill();
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Unique
    private void salis_Arcana$removeAndSplit(int removeCount) {
        if (aspects.size() == 0 || removeCount <= 1) return;
        Random rand = worldObj.rand;

        int splitRemoval = (int) Math.ceil((double) removeCount / aspects.size());

        int toRemove;
        int remainder = 0;
        int totalRemoved = 0;
        while (totalRemoved < removeCount) {
            Aspect[] availableAspects = aspects.getAspects();
            Aspect a = availableAspects[rand.nextInt(availableAspects.length)];
            if (a.isPrimal()) {
                a = availableAspects[rand.nextInt(availableAspects.length)];
            }
            toRemove = Math.min(splitRemoval + remainder, removeCount - totalRemoved);
            if (toRemove > aspects.getAmount(a)) {
                remainder = toRemove - aspects.getAmount(a);
                toRemove = aspects.getAmount(a);
            } else {
                remainder = 0;
            }
            aspects.remove(a, toRemove);
            if (!a.isPrimal()) {
                if (rand.nextBoolean()) {
                    aspects.add(a.getComponents()[0], toRemove);
                } else {
                    aspects.add(a.getComponents()[1], toRemove);
                }
            }
            totalRemoved += toRemove;
        }
    }
}
