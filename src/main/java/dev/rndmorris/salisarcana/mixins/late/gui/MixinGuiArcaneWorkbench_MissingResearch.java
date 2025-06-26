package dev.rndmorris.salisarcana.mixins.late.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.api.IMultipleResearchArcaneRecipe;
import dev.rndmorris.salisarcana.lib.CraftingHelper;
import dev.rndmorris.salisarcana.lib.KnowItAll;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(GuiArcaneWorkbench.class)
public abstract class MixinGuiArcaneWorkbench_MissingResearch extends GuiContainer {

    public MixinGuiArcaneWorkbench_MissingResearch(Container p_i1072_1_) {
        super(p_i1072_1_);
    }

    @WrapOperation(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingArcaneRecipe(Lnet/minecraft/inventory/IInventory;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;",
            remap = false))
    public ItemStack captureRecipe(IInventory awb, EntityPlayer player, Operation<ItemStack> original,
        @Local(name = "var5") int centerX, @Local(name = "var6") int topY, @Local ItemWandCasting wand) {

        if (wand == null) return null;

        final var recipe = CraftingHelper.INSTANCE.findArcaneRecipe(awb, KnowItAll.getInstance());

        if (recipe != null && !recipe.matches(awb, player.worldObj, player)) {
            final var researchArray = (recipe instanceof IMultipleResearchArcaneRecipe multi)
                ? multi.salisArcana$getResearches(awb, player.worldObj, player)
                : new String[] { recipe.getResearch() };

            GL11.glPushMatrix();
            GL11.glTranslatef(centerX + 168, topY, 0f);
            GL11.glScalef(0.5f, 0.5f, 0f);

            int y = 185;
            for (final var key : researchArray) {
                final var research = ResearchCategories.getResearch(key);
                if (research != null && !ResearchManager.isResearchComplete(player.getCommandSenderName(), key)) {
                    final var lines = this.fontRendererObj.listFormattedStringToWidth(research.getName(), 120);
                    for (final var line : lines) {
                        this.fontRendererObj
                            .drawString(line, this.fontRendererObj.getStringWidth(line) / -2, y, 0xEE6E6E);
                        y += this.fontRendererObj.FONT_HEIGHT + 2;
                    }
                    y += 3;
                }
            }

            if (y > 185) {
                String error = StatCollector.translateToLocal("salisarcana:error_missing_research.gui");
                this.fontRendererObj
                    .drawString(error, this.fontRendererObj.getStringWidth(error) / -2, 46 * 2, 0xEE6E6E);
            }

            GL11.glPopMatrix();
            return null;
        }

        return recipe == null ? null : recipe.getCraftingResult(awb);
    }
}
