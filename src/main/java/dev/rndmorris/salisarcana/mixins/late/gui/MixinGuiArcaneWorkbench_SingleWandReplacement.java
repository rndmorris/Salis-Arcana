package dev.rndmorris.salisarcana.mixins.late.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import dev.rndmorris.salisarcana.lib.KnowItAll;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(GuiArcaneWorkbench.class)
public class MixinGuiArcaneWorkbench_SingleWandReplacement {

    @Shadow(remap = false)
    private TileArcaneWorkbench tileEntity;

    @Shadow(remap = false)
    private InventoryPlayer ip;

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer", at = @At(value = "STORE", ordinal = 0))
    public ItemWandCasting captureWandInTable(final ItemWandCasting initial,
        final @Share("wandStack") LocalRef<ItemStack> wandStack) {

        if (this.tileEntity.getStackInSlot(10) == null) {
            // No wand in wand slot - check if a wand-swap recipe matches
            for (int i = 0; i < 9; i++) {
                final ItemStack slot = this.tileEntity.getStackInSlot(i);
                if (slot != null && slot.getItem() instanceof ItemWandCasting itemWand) {
                    // Found a wand in table
                    if ((CustomRecipes.replaceWandCapsRecipe != null && CustomRecipes.replaceWandCapsRecipe
                        .matches(this.tileEntity, this.ip.player.worldObj, KnowItAll.getInstance()))
                        || (CustomRecipes.replaceWandCoreRecipe != null && CustomRecipes.replaceWandCoreRecipe
                            .matches(this.tileEntity, this.ip.player.worldObj, KnowItAll.getInstance()))) {
                        // Recipe match found
                        wandStack.set(slot);
                        return itemWand;
                    } else {
                        // There's a wand, but none of the recipes match
                        return null;
                    }
                }
            }
            return null;
        } else {
            return initial;
        }
    }

    @ModifyExpressionValue(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/tiles/TileArcaneWorkbench;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingArcaneRecipeAspects(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/aspects/AspectList;",
                remap = false)))
    public ItemStack replaceWandStack(final ItemStack initial,
        final @Share("wandStack") LocalRef<ItemStack> wandStack) {
        return initial == null ? wandStack.get() : initial;
    }
}
