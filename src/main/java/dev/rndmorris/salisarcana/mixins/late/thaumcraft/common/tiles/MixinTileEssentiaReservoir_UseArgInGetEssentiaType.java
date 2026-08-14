package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.tiles.TileEssentiaReservoir;

@Mixin(value = TileEssentiaReservoir.class, remap = false)
abstract class MixinTileEssentiaReservoir_UseArgInGetEssentiaType {

    @ModifyExpressionValue(
        method = "getEssentiaType",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraftforge/common/util/ForgeDirection;UNKNOWN:Lnet/minecraftforge/common/util/ForgeDirection;"))
    private ForgeDirection useFaceInstead(ForgeDirection original, @Local(argsOnly = true) ForgeDirection loc) {
        return loc;
    }

}
