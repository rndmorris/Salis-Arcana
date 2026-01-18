package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.fx.beams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.lib.WandHelper;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandRod;
import thaumcraft.client.fx.beams.FXBeamWand;

@Mixin(value = FXBeamWand.class, remap = false)
public class MixinFXBeamWand_FociStaffVisualFix {

    @Shadow
    private double offset;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void updateOffset(World par1World, EntityPlayer player, double tx, double ty, double tz, float red,
        float green, float blue, int age, CallbackInfo ci) {
        WandRod rod = WandHelper.getWandRodFromWand(player.getHeldItem());
        if (rod instanceof StaffRod) {
            this.offset += .07D;
        }
    }
}
