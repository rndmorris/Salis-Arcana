package dev.rndmorris.salisarcana.mixins.late.addons.ThaumcraftNEIPlugin;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.djgiannuzz.thaumcraftneiplugin.nei.overlay.ArcaneWorkbenchOverlayHandler;

import codechicken.nei.recipe.DefaultOverlayHandler;
import thaumcraft.common.container.ContainerArcaneWorkbench;

@Mixin(value = ArcaneWorkbenchOverlayHandler.class, remap = false)
public class MixinArcaneWorkbenchOverlayHandler {

    @Inject(method = "moveIngredients", at = @At("TAIL"))
    public void triggerRecipeSearch(GuiContainer gui,
        List<DefaultOverlayHandler.IngredientDistribution> assignedIngredients, int quantity, CallbackInfo ci) {
        if (gui.inventorySlots instanceof ContainerArcaneWorkbench workbench) {
            workbench.onCraftMatrixChanged(null);
        }
    }
}
