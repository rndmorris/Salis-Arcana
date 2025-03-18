package dev.rndmorris.salisarcana.mixins.late.tiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.tiles.TileInfusionMatrix;

import java.util.ArrayList;

@Mixin(TileInfusionMatrix.class)
public class MixinTileInfusionMatrix_MissingResearch {
    @WrapOperation(method = "craftingStart", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingInfusionRecipe(Ljava/util/ArrayList;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/crafting/InfusionRecipe;", remap = false))
    public InfusionRecipe captureInfusionRecipe(ArrayList<ItemStack> components, ItemStack centerItem, EntityPlayer player, Operation<InfusionRecipe> original) {
        final var recipe = original.call(components, centerItem, ResearchHelper.knowItAll());

        if(recipe != null && !recipe.matches(components, centerItem, player.worldObj, player)) {
            ResearchHelper.sendResearchError(player, recipe.getResearch());
            return null;
        }

        return recipe;
    }

    @WrapOperation(method = "craftingStart", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingInfusionEnchantmentRecipe(Ljava/util/ArrayList;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)Lthaumcraft/api/crafting/InfusionEnchantmentRecipe;", remap = false))
    public InfusionEnchantmentRecipe captureInfusionEnchantmentRecipe(ArrayList<ItemStack> components, ItemStack centerItem, EntityPlayer player, Operation<InfusionEnchantmentRecipe> original) {
        final var recipe = original.call(components, centerItem, ResearchHelper.knowItAll());

        if(recipe != null && !recipe.matches(components, centerItem, player.worldObj, player)) {
            ResearchHelper.sendResearchError(player, recipe.getResearch());
            return null;
        }

        return recipe;
    }
}
