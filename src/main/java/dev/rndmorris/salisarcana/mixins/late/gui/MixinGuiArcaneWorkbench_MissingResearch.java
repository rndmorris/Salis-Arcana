package dev.rndmorris.salisarcana.mixins.late.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rndmorris.salisarcana.lib.CraftingHelper;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.client.gui.GuiArcaneWorkbench;

@Mixin(GuiArcaneWorkbench.class)
public class MixinGuiArcaneWorkbench_MissingResearch {
    @WrapOperation(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingArcaneRecipe(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;", remap = false))
    public ItemStack captureRecipe(IInventory awb, EntityPlayer player, Operation<ItemStack> original) {
        final var recipe = CraftingHelper.INSTANCE.findArcaneRecipe(awb, ResearchHelper.knowItAll());

        if(recipe != null && !recipe.matches(awb, player.worldObj, player)) {
            // TODO Implement multi-research list &
            ResearchHelper.sendResearchError(player, recipe.getResearch());
        }

        return recipe == null ? null : recipe.getCraftingResult(awb);
    }
}
