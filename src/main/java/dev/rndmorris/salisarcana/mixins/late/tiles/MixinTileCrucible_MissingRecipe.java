package dev.rndmorris.salisarcana.mixins.late.tiles;

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

import dev.rndmorris.salisarcana.lib.KnowItAll;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
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
        final var recipe = original.call(KnowItAll.getUsername(), aspects, lastItem);

        if (recipe != null && !ResearchManager.isResearchComplete(username, recipe.key)) {
            final var player = this.worldObj.getPlayerEntityByName(username);
            LocalDateTime lastUsageTime;
            if (player != null) {
                lastUsageTime = sa$warnings.computeIfAbsent(player, (_key) -> new ConcurrentHashMap<>())
                    .get(recipe);
                // if the player hasn't been warned since they last logged in, OR if it's been five minutes
                // since the player last tossed something in that matches this recipe
                if (lastUsageTime == null || lastUsageTime.isBefore(
                    LocalDateTime.now()
                        .minusMinutes(5))) {
                    ResearchHelper.sendResearchError(player, recipe.key, "salisarcana:error_missing_research.crucible");
                }
                // keeps the warning tied to a rolling window, to minimize spamming
                sa$warnings.computeIfAbsent(player, (_key) -> new ConcurrentHashMap<>())
                    .put(recipe, LocalDateTime.now());
            }
            return null;
        }

        return recipe;
    }
}
