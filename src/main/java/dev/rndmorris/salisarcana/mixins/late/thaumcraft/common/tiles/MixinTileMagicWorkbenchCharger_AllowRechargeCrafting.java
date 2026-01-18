package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import thaumcraft.common.tiles.TileMagicWorkbench;
import thaumcraft.common.tiles.TileMagicWorkbenchCharger;

@Mixin(TileMagicWorkbenchCharger.class)
public class MixinTileMagicWorkbenchCharger_AllowRechargeCrafting {

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/ItemWandCasting;addRealVis(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/aspects/Aspect;IZ)I",
            remap = false))
    public void captureWorkbench(CallbackInfo ci, @Local TileMagicWorkbench workbench,
        @Share("workbench") LocalRef<TileMagicWorkbench> workbenchRef) {
        // Only gets called on the logical server whenever an aspect is charged into the wand.
        workbenchRef.set(workbench);
    }

    @Inject(method = "updateEntity", at = @At("TAIL"))
    public void updateGUI(CallbackInfo ci, @Share("workbench") LocalRef<TileMagicWorkbench> workbenchRef) {
        TileMagicWorkbench workbench = workbenchRef.get();

        if (workbench != null && workbench.eventHandler != null) {
            workbench.eventHandler.onCraftMatrixChanged(workbench);
        }
    }
}
