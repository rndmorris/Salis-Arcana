package dev.rndmorris.salisarcana.mixins.late.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.lib.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(ContainerArcaneWorkbench.class)
public abstract class MixinContainerArcaneWorkbench_SingleWandReplacement extends Container {

    @Shadow(remap = false)
    private TileArcaneWorkbench tileEntity;

    @Shadow(remap = false)
    private InventoryPlayer ip;

    @Inject(
        method = "onCraftMatrixChanged",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;setInventorySlotContentsSoftly(ILnet/minecraft/item/ItemStack;)V",
            ordinal = 0,
            shift = At.Shift.AFTER,
            remap = false))
    public void useGridWandForReplacement(CallbackInfo ci) {
        // Exclusive with the normal arcane recipe check, since this requires no wand present
        if (this.getSlot(0)
            .getStack() == null && this.tileEntity.getStackInSlot(10) == null) {
            // If a replacement recipe matches
            ItemStack outputWand;
            AspectList visPrice;

            if (CustomRecipes.replaceWandCapsRecipe != null && CustomRecipes.replaceWandCapsRecipe
                .matches(this.tileEntity, this.ip.player.worldObj, this.ip.player)) {
                outputWand = CustomRecipes.replaceWandCapsRecipe.getCraftingResult(this.tileEntity);
                visPrice = CustomRecipes.replaceWandCapsRecipe.getAspects(this.tileEntity);
            } else if (CustomRecipes.replaceWandCoreRecipe != null && CustomRecipes.replaceWandCoreRecipe
                .matches(this.tileEntity, this.ip.player.worldObj, this.ip.player)) {
                    outputWand = CustomRecipes.replaceWandCoreRecipe.getCraftingResult(this.tileEntity);
                    visPrice = CustomRecipes.replaceWandCoreRecipe.getAspects(this.tileEntity);
                } else {
                    return;
                }

            // Mostly here so my compiler would stop yelling at me.
            if (!(outputWand.getItem() instanceof ItemWandCasting itemWand)) return;

            ItemStack originalWand = null;
            for (int i = 0; i < 9; i++) {
                final ItemStack slot = this.tileEntity.getStackInSlot(i);
                if (slot != null && slot.getItem() instanceof ItemWandCasting) {
                    originalWand = slot.copy();
                    break;
                }
            }

            // Vis is spent using the original wand, which is why that loop above is necessary.
            if (itemWand.consumeAllVisCrafting(originalWand, this.ip.player, visPrice, true)) {
                if (ConfigModuleRoot.enhancements.preserveWandVis.isEnabled()) {
                    // Copied from ReplaceWandCoreRecipe. Thank you, Morris.
                    final var maxVis = itemWand.getMaxVis(outputWand);
                    final var newVis = new AspectList();
                    final var originalVis = itemWand.getAllVis(originalWand);
                    for (var entry : originalVis.aspects.entrySet()) {
                        newVis.add(entry.getKey(), Integer.min(maxVis, entry.getValue()));
                    }
                    itemWand.storeAllVis(outputWand, newVis);
                } else {
                    itemWand.storeAllVis(outputWand, AspectHelper.primalList(0));
                }

                if (ConfigModuleRoot.bugfixes.arcaneWorkbenchMultiContainer.isEnabled()) {
                    this.getSlot(0)
                        .putStack(outputWand);
                } else {
                    this.tileEntity.setInventorySlotContentsSoftly(9, outputWand);
                }
            }
        }
    }
}
