package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.CrucibleHeatLogic;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class)
public abstract class MixinTileCrucible_HeatSources extends TileThaumcraft {

    /**
     * Tap into the {@code mat == Material.lava} comparison and, if it's not a vanilla thaum heat source, check if the
     * block meets our additional
     * acceptable criteria.
     * Because of our mixin point, we can force a successful "can heat crucible" check by returning {@code mat} from the
     * original method, since it should always equal itself.
     */
    @WrapOperation(
        method = "updateEntity",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/block/material/Material;lava:Lnet/minecraft/block/material/Material;"))
    private Material checkHeatSource(Operation<Material> original, @Local(name = "bi") Block block,
        @Local(name = "md") int md, @Local(name = "mat") Material blockMaterial) {
        return CrucibleHeatLogic.isCrucibleHeatSource(block, md, blockMaterial) ? blockMaterial : original.call();
    }

}
