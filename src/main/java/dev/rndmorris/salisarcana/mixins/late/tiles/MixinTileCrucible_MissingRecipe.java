package dev.rndmorris.salisarcana.mixins.late.tiles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.rndmorris.salisarcana.lib.ResearchHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(TileCrucible.class)
public class MixinTileCrucible_MissingRecipe extends TileEntity {
    @WrapOperation(method = "attemptSmelt", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;findMatchingCrucibleRecipe(Ljava/lang/String;Lthaumcraft/api/aspects/AspectList;Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/crafting/CrucibleRecipe;", remap = false))
    public CrucibleRecipe captureCrucibleRecipe(String username, AspectList aspects, ItemStack lastItem, Operation<CrucibleRecipe> original, @Share("sentMessage") LocalBooleanRef sentMessage) {
        final var recipe = original.call(ResearchHelper.knowItAll().getCommandSenderName(), aspects, lastItem);

        if(recipe != null && !ResearchManager.isResearchComplete(username, recipe.key)) {
            if(!sentMessage.get()) {
                final var player = this.worldObj.getPlayerEntityByName(username);
                if(player != null) ResearchHelper.sendResearchError(player, recipe.key);
                sentMessage.set(true);
            }
            return null;
        }

        return recipe;
    }
}
