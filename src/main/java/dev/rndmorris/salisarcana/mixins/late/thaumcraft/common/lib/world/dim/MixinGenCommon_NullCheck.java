package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.world.dim;

import net.minecraft.world.World;
import net.sf.cglib.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.lib.world.dim.GenCommon;
import thaumcraft.common.tiles.TileCrystal;

@Mixin(value = GenCommon.class, remap = false)
public abstract class MixinGenCommon_NullCheck {

    @Inject(
        method = "processDecorations",
        at = @At(
            value = "FIELD",
            target = "Lthaumcraft/common/tiles/TileCrystal;orientation:S",
            opcode = Opcodes.PUTFIELD),
        cancellable = true)
    private static void preventNullOrientation(World world, CallbackInfo ci, @Local(name = "te") TileCrystal te) {
        if (te == null) {
            ci.cancel();
        }
    }
}
