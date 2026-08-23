package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.tiles.TileEssentiaReservoir;

@Mixin(value = TileEssentiaReservoir.class, remap = false)
abstract class MixinTileEssentiaReservoir_UseArgInGetEssentiaType {

    @ModifyExpressionValue(
        method = "getEssentiaType",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraftforge/common/util/ForgeDirection;UNKNOWN:Lnet/minecraftforge/common/util/ForgeDirection;",
            opcode = Opcodes.GETSTATIC))
    private ForgeDirection useFaceInstead(ForgeDirection original, ForgeDirection loc) {
        return loc;
    }

}
