package dev.rndmorris.salisarcana.mixins.late.tiles;

import java.util.ArrayList;

import net.minecraft.util.ChunkCoordinates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.StabilizerLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
public class MixinTileInfusionMatrix_IntegerStabilizers extends TileThaumcraft {

    @Shadow
    public int symmetry;

    @Inject(
        method = "getSurroundings",
        at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 0),
        cancellable = true)
    private void calculateStabilizers(CallbackInfo ci, @Local ArrayList<ChunkCoordinates> stuff) {
        // inject immediately before the matrix starts calculating infusion stabilizers
        var instabilityModifier = 0;

        for (ChunkCoordinates cc : stuff) {
            if (StabilizerLogic.isStabilizer(worldObj, cc.posX, cc.posY, cc.posZ)) {
                instabilityModifier += 1;
            }
            final var oppX = xCoord + (xCoord - cc.posX);
            final var oppZ = zCoord + (zCoord - cc.posZ);
            if (StabilizerLogic.isStabilizer(worldObj, oppX, cc.posY, oppZ)) {
                instabilityModifier -= 2;
            }
        }

        // rounds towards 0, should be functionally identical to the original rounding
        this.symmetry += instabilityModifier / 10;

        ci.cancel();
    }
}
