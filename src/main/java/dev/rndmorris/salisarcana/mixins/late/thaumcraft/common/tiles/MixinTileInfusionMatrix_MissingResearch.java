package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.KnowItAll;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(TileInfusionMatrix.class)
public class MixinTileInfusionMatrix_MissingResearch {

    @WrapOperation(
        method = "craftingStart",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingInfusionRecipe(Ljava/util/ArrayList;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/crafting/InfusionRecipe;",
            remap = false),
        remap = false)
    public InfusionRecipe captureInfusionRecipe(ArrayList<ItemStack> components, ItemStack centerItem,
        EntityPlayer player, Operation<InfusionRecipe> original) {
        final var recipe = original.call(components, centerItem, KnowItAll.getInstance());

        if (recipe != null && !recipe.matches(components, centerItem, player.worldObj, player)) {
            ResearchHelper
                .sendResearchError(player, recipe.getResearch(), "salisarcana:error_missing_research.infusion");
            return null;
        }

        return recipe;
    }

    @WrapOperation(
        method = "craftingStart",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingInfusionEnchantmentRecipe(Ljava/util/ArrayList;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/crafting/InfusionEnchantmentRecipe;",
            remap = false),
        remap = false)
    public InfusionEnchantmentRecipe captureInfusionEnchantmentRecipe(ArrayList<ItemStack> components,
        ItemStack centerItem, EntityPlayer player, Operation<InfusionEnchantmentRecipe> original) {
        final var recipe = original.call(components, centerItem, KnowItAll.getInstance());

        if (recipe != null && !recipe.matches(components, centerItem, player.worldObj, player)) {
            ResearchHelper
                .sendResearchError(player, recipe.getResearch(), "salisarcana:error_missing_research.infusion");
            return null;
        }

        return recipe;
    }
}
