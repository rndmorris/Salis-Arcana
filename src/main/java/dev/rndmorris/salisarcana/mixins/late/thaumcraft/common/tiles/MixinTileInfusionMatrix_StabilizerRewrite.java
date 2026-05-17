package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.util.ChunkCoordinates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import dev.rndmorris.salisarcana.lib.InfusionMatrixLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
public class MixinTileInfusionMatrix_StabilizerRewrite extends TileThaumcraft {

    @Shadow
    private ArrayList<ChunkCoordinates> pedestals;

    @Shadow
    public int symmetry;

    /**
     * @author rndmorris
     * @reason We're making large, invasive changes to how and what the runic matrix considers a stabilizer
     */
    @Overwrite
    private void getSurroundings() {
        final var result = InfusionMatrixLogic.checkMatrixSurroundings(worldObj, xCoord, yCoord, zCoord);
        symmetry = result.symmetry;
        pedestals.clear();
        pedestals.addAll(result.pedestals);
    }

}
