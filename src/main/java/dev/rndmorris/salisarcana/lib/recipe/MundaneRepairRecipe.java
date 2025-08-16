package dev.rndmorris.salisarcana.lib.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public final class MundaneRepairRecipe implements IRecipe {

    public static final MundaneRepairRecipe INSTANCE = new MundaneRepairRecipe();

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return this.getCraftingResult(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack toolOne = null;
        ItemStack toolTwo = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            final ItemStack stack = inv.getStackInSlot(i);

            if (stack == null) continue;

            if (stack.stackSize != 1) {
                // Tools must have a stack-size of 1 (according to the Minecraft implementation)
                return null;
            }

            if (toolOne == null) {
                toolOne = stack;

                // noinspection DataFlowIssue
                if (!stack.getItem()
                    .isRepairable()) {
                    // Tool cannot be repaired in workbench
                    return null;
                }
            } else if (toolTwo == null) {
                toolTwo = stack;

                if (toolOne.getItem() != toolTwo.getItem()) {
                    // Mismatched items
                    return null;
                }
            } else {
                // More than two items found
                return null;
            }
        }

        if (toolTwo != null) {
            // Two matching items found
            final Item item = toolOne.getItem();
            final int maxDamage = item.getMaxDamage();

            int durability = maxDamage - toolOne.getItemDamageForDisplay();
            durability += maxDamage - toolTwo.getItemDamageForDisplay();
            durability += maxDamage / 20; // 5% bonus

            return new ItemStack(item, 1, Math.max(0, maxDamage - durability));
        } else {
            // One or zero matching items found
            return null;
        }
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
