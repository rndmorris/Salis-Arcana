package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

        for (int i = 0; i < removeCount - 1; i++) {
            if (aspects.size() == 0) break;

            Aspect[] availableAspects = aspects.getAspects();
            Aspect a = availableAspects[worldObj.rand.nextInt(availableAspects.length)];
            if (a.isPrimal()) {
                a = availableAspects[worldObj.rand.nextInt(availableAspects.length)];
            }
            aspects.remove(a, 1);

            if (!a.isPrimal()) {
                if (worldObj.rand.nextBoolean()) {
                    aspects.add(a.getComponents()[0], 1);
                } else {
                    aspects.add(a.getComponents()[1], 1);
                }
            }
        }

        int spills = Math.min(1 + (removeCount / 10), 5);
        for (int i = 0; i < spills; i++) {
            spill();
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
