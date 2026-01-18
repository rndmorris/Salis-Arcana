package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraftforge.fluids.FluidTank;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class, remap = false)
public class MixinTileCrucible_EarlyTerminateCraft {

    @Shadow
    public FluidTank tank;

    @ModifyVariable(
        method = "attemptSmelt",
        name = "a",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/fluids/FluidTank;drain(IZ)Lnet/minecraftforge/fluids/FluidStack;",
            shift = At.Shift.AFTER))
    private int terminateLoop(int original) {
        // It will get incremented, so we don't want it to roll over.
        return this.tank.getFluidAmount() > 0 ? original : (Integer.MAX_VALUE - 1);
    }
}
