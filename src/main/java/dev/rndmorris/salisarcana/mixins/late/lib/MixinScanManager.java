package dev.rndmorris.salisarcana.mixins.late.lib;

import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;

@Mixin(value = ScanManager.class, remap = false)
public class MixinScanManager {

    @Inject(
        method = "completeScan",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;getObjectTags(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;",
            ordinal = 0),
        cancellable = true)
    private static void onScanComplete(EntityPlayer player, ScanResult scan, String prefix,
        CallbackInfoReturnable<Boolean> cir) {
        if (!ScanManager.isValidScanTarget(player, scan, "@")) {
            cir.cancel();
        }
    }
}
