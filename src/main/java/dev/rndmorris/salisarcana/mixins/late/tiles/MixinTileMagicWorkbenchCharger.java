package dev.rndmorris.salisarcana.mixins.late.tiles;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.tiles.TileMagicWorkbench;
import thaumcraft.common.tiles.TileMagicWorkbenchCharger;

@Mixin(TileMagicWorkbenchCharger.class)
public class MixinTileMagicWorkbenchCharger {
    @Inject(method = "updateEntity", at = @At(value = "INVOKE", target = "Lthaumcraft/common/items/wands/ItemWandCasting;addRealVis(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/aspects/Aspect;IZ)I"))
    public void captureWorkbench(CallbackInfo ci, @Local TileMagicWorkbench workbench, @Share("workbench") LocalRef<TileMagicWorkbench> workbenchRef) {
        workbenchRef.set(workbench);
    }

    @Inject(method = "updateEntity", at = @At("TAIL"))
    public void updateGUI(CallbackInfo ci, @Share("workbench") LocalRef<TileMagicWorkbench> workbenchRef) {
        TileMagicWorkbench workbench = workbenchRef.get();

        if(workbench != null && workbench.eventHandler != null && workbench.getStackInSlot(9) == null) {
            workbench.eventHandler.onCraftMatrixChanged(workbench);
        }
    }
}
