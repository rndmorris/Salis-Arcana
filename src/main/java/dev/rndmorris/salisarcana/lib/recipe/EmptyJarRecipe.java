package dev.rndmorris.salisarcana.lib.recipe;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigBlocks;

public class EmptyJarRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        ArrayList<ItemStack> stacks = getStacks(inventory);
        if (stacks.size() != 1) {
            return false; // We only allow one item in the crafting grid
        }

        ItemStack itemStack = stacks.get(0);
        return itemStack.getItem() instanceof ItemJarFilled;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ArrayList<ItemStack> stacks = getStacks(inventory);
        ItemStack stack = stacks.get(0);
        if (stack.getItem() instanceof ItemJarFilled jar) {
            Aspect filter = jar.getFilter(stack);
            ItemStack emptyJar;
            if (filter != null) {
                emptyJar = new ItemStack(jar, 1, stack.getItemDamage());
                // If the jar has a filter, we return an empty jar with the filter intact
                emptyJar.setTagCompound(new NBTTagCompound());
                emptyJar.getTagCompound()
                    .setString("AspectFilter", filter.getTag());
            } else {
                // If the jar doesn't have a filter, we return a warded jar instead of jar of essentia
                emptyJar = new ItemStack(ConfigBlocks.blockJar, 1, stack.getItemDamage());
            }
            return emptyJar;
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    private ArrayList<ItemStack> getStacks(InventoryCrafting inventory) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() != null) {
                stacks.add(stack);
            }
        }
        return stacks;
    }
}
