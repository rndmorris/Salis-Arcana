package dev.rndmorris.salisarcana.mixins.late.gui;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.rndmorris.salisarcana.common.recipes.CustomRecipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(GuiArcaneWorkbench.class)
public class MixinGuiArcaneWorkbench_SingleWandReplacement {
    @Shadow(remap = false)
    private TileArcaneWorkbench tileEntity;

    @Shadow(remap = false)
    private InventoryPlayer ip;

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingArcaneRecipeAspects(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/aspects/AspectList;", remap = false))
    public ItemWandCasting captureWandInTable(final ItemWandCasting slot10, final @Share("wandStack") LocalRef<ItemStack> wandStack) {
        if(slot10 == null) { // and Arcane Recipe found
            for(int i=0; i<9; i++) {
                final ItemStack slot = this.tileEntity.getStackInSlot(i);
                if(slot.getItem() instanceof ItemWandCasting itemWand) {
                    // Found a wand in table
                    if((CustomRecipes.replaceWandCapsRecipe != null && CustomRecipes.replaceWandCapsRecipe.matches(this.tileEntity, this.ip.player.worldObj, this.ip.player))
                        || (CustomRecipes.replaceWandCoreRecipe != null && CustomRecipes.replaceWandCoreRecipe.matches(this.tileEntity, this.ip.player.worldObj, this.ip.player))) {
                        wandStack.set(slot);
                        return itemWand;
                    }
                }
            }
            return null;
        } else {
            return slot10;
        }
    }

    @ModifyArg(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lthaumcraft/common/items/wands/ItemWandCasting;getConsumptionModifier(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/Aspect;Z)F", remap = false), index = 0)
    public ItemStack replaceWandConsumption(final ItemStack initial, final @Share("wandStack") LocalRef<ItemStack> wandStack) {
        return initial == null ? wandStack.get() : initial;
    }
    @ModifyArg(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lthaumcraft/common/items/wands/ItemWandCasting;getVis(Lnet/minecraft/item/ItemStack;Lthaumcraft/api/aspects/Aspect;)I", remap = false), index = 0)
    public ItemStack replaceWandVis(final ItemStack initial, final @Share("wandStack") LocalRef<ItemStack> wandStack) {
        return initial == null ? wandStack.get() : initial;
    }
}
