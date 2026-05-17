package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
public class MixinTileInfusionMatrix_InputEnforcement {

    @WrapOperation(
        method = "craftCycle",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/utils/InventoryUtils;areItemStacksEqualForCrafting(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;ZZZ)Z"))
    private boolean mixinCraftCycle(ItemStack fromPedestal, ItemStack recipeInput, boolean useOre, boolean ignoreDamage,
        boolean ignoreNBT, Operation<Boolean> original) {

        // we just need to know that the input item and the item on the pedestal are the exact same item
        // ore dict stuff is irrelevant here

        if (fromPedestal == null || recipeInput == null) {
            return false;
        }

        if (!recipeInput.isItemEqual(fromPedestal)) {
            return false;
        }

        final var inputTag = recipeInput.stackTagCompound;
        final var pedestalTag = fromPedestal.stackTagCompound;

        if ((inputTag != null && pedestalTag == null) || (inputTag == null && pedestalTag != null)) {
            return false;
        }

        if (inputTag == null) {
            return true;
        }

        return inputTag.equals(pedestalTag);
    }
}
