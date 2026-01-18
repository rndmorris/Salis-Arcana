package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.container;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.lib.ifaces.ICachedMagicWorkbench;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@Mixin(ContainerArcaneWorkbench.class)
public class MixinContainerArcaneWorkbench_UseCache {

    @Shadow(remap = false)
    private TileArcaneWorkbench tileEntity;

    @WrapOperation(
        method = "onCraftMatrixChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/crafting/CraftingManager;findMatchingRecipe(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack useCachedRecipe(CraftingManager instance, InventoryCrafting inventoryCrafting, World world,
        Operation<ItemStack> original) {
        if (this.tileEntity instanceof ICachedMagicWorkbench cache) {
            final var recipe = cache.salisArcana$getMundaneRecipe();
            return recipe != null ? recipe.getCraftingResult(inventoryCrafting) : null;
        }

        return original.call(instance, inventoryCrafting, world);
    }
}
