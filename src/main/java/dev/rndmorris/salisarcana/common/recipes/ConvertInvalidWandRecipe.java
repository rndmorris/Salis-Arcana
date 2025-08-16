package dev.rndmorris.salisarcana.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import dev.rndmorris.salisarcana.lib.WandHelper;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ConvertInvalidWandRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventory, World p_77569_2_) {
        return findInvalidWand(inventory) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack wandStack = findInvalidWand(inventory);
        if (wandStack == null) return null;

        final ItemWandCasting wand = (ItemWandCasting) wandStack.getItem();
        wandStack = wandStack.copy();

        // noinspection DataFlowIssue
        if (wand.getCap(wandStack) == WandHelper.CAP_UNKNOWN) {
            wandStack.stackTagCompound.removeTag("cap");
        }

        final var rod = wand.getRod(wandStack);
        if (rod == WandHelper.ROD_UNKNOWN || rod == WandHelper.STAFF_UNKNOWN) {
            wandStack.stackTagCompound.removeTag("rod");
        }

        return wandStack;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    private static ItemStack findInvalidWand(InventoryCrafting inventory) {
        ItemStack wandStack = null;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            final ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null) {
                if (wandStack != null) {
                    // Must have only one item in the crafting window
                    return null;
                }
                if (!(stack.getItem() instanceof ItemWandCasting wand)) {
                    // That item must be a wand
                    return null;
                }

                final var cap = wand.getCap(stack);
                final var rod = wand.getRod(stack);

                if (cap != WandHelper.CAP_UNKNOWN && rod != WandHelper.ROD_UNKNOWN && rod != WandHelper.STAFF_UNKNOWN) {
                    // That wand must be invalid
                    return null;
                }

                wandStack = stack;
            }
        }

        return wandStack;
    }
}
