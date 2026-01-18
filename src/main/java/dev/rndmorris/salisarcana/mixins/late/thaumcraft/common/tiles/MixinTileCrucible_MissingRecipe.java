package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.ResearchHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(TileCrucible.class)
public class MixinTileCrucible_MissingRecipe extends TileEntity {

    // player -> crucible recipe -> last time the player matched that recipe
    // WeakHashMap ensures that the data being tracked can get garbage collected after the player entity does (such as
    // when they log off)
    @Unique
    private static final Map<EntityPlayer, ConcurrentHashMap<CrucibleRecipe, LocalDateTime>> sa$warnings = Collections
        .synchronizedMap(new WeakHashMap<>());

    @WrapOperation(
        method = "attemptSmelt",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingCrucibleRecipe(Ljava/lang/String;Lthaumcraft/api/aspects/AspectList;Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/crafting/CrucibleRecipe;",
            remap = false),
        remap = false)
    public CrucibleRecipe captureCrucibleRecipe(String username, AspectList aspects, ItemStack lastItem,
        Operation<CrucibleRecipe> original) {
        int bestRecipeScore = -1;
        CrucibleRecipe bestRecipe = null;

        for (Object obj : ThaumcraftApi.getCraftingRecipes()) {
            if (!(obj instanceof CrucibleRecipe recipe)) continue;
            if (!recipe.matches(aspects, lastItem)) continue;

            if (ResearchManager.isResearchComplete(username, recipe.key)) {
                // Recipe matches & is craftable, implement standard recipe search logic
                final int score = recipe.aspects.size();
                if (score > bestRecipeScore) {
                    bestRecipeScore = score;
                    bestRecipe = recipe;
                }
            } else {
                // Missing research for possible recipe, report to player.
                final var player = this.worldObj.getPlayerEntityByName(username);
                LocalDateTime lastUsageTime;
                if (player != null) {
                    lastUsageTime = sa$warnings.computeIfAbsent(player, _key -> new ConcurrentHashMap<>())
                        .get(recipe);
                    // if the player hasn't been warned since they last logged in, OR if it's been five minutes
                    // since the player last tossed something in that matches this recipe
                    if (lastUsageTime == null || lastUsageTime.isBefore(
                        LocalDateTime.now()
                            .minusMinutes(5))) {
                        ResearchHelper
                            .sendResearchError(player, recipe.key, "salisarcana:error_missing_research.crucible");
                    }
                    // keeps the warning tied to a rolling window, to minimize spamming
                    sa$warnings.get(player)
                        .put(recipe, LocalDateTime.now());
                }
            }
        }

        return bestRecipe;
    }
}
