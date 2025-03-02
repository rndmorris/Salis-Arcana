package dev.rndmorris.salisarcana.mixins.late.addons.thaumcraftneiplugin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.djgiannuzz.thaumcraftneiplugin.nei.recipehandler.InfusionRecipeHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.common.recipes.NeiOnlyRecipeRegistry;

@Mixin(value = InfusionRecipeHandler.class, remap = false)
public abstract class MixinInfusionRecipeHandler_FakeRecipes {

    @WrapOperation(
        method = { "loadCraftingRecipes(Ljava/lang/String;[Ljava/lang/Object;)V",
            "loadCraftingRecipes(Lnet/minecraft/item/ItemStack;)V",
            "loadUsageRecipes(Lnet/minecraft/item/ItemStack;)V" },
        at = @At(value = "INVOKE", target = "Lthaumcraft/api/ThaumcraftApi;getCraftingRecipes()Ljava/util/List;"))
    private List<Object> wrapLoadCraftingRecipes(Operation<List<Object>> original) {
        return Stream.concat(
            original.call()
                .stream(),
            NeiOnlyRecipeRegistry.getInstance().infusionRecipes.stream())
            .collect(Collectors.toList());
    }

}
