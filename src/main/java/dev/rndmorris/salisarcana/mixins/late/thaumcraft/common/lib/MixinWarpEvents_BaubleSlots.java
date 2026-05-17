package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.llamalad7.mixinextras.sugar.Local;

import baubles.api.BaublesApi;
import thaumcraft.common.lib.WarpEvents;

@Mixin(WarpEvents.class)
public class MixinWarpEvents_BaubleSlots {

    @ModifyConstant(method = "getWarpFromGear", constant = @Constant(intValue = 4, ordinal = 1), remap = false)
    private static int useAllBaubleSlots(int constant, @Local(name = "player") EntityPlayer player) {
        return BaublesApi.getBaubles(player)
            .getSizeInventory();
    }
}
