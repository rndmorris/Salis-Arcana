package dev.rndmorris.salisarcana.lib.customresearch;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.rndmorris.salisarcana.lib.StringHelper;

public class ItemEntry {

    public ItemEntry() {

    }

    public ItemEntry(ItemStack stack) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        this.meta = stack.getItemDamage();
        this.amount = stack.stackSize;
        if (identifier == null) {
            return;
        }
        this.item = identifier.toString();
    }

    public String item;
    public int meta;
    public int amount;

    public String getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return getItemStack(this);
    }

    public int getMeta() {
        return meta;
    }

    public int getAmount() {
        return amount;
    }

    public static ItemStack getItemStack(ItemEntry entry) {
        ItemStack stack = StringHelper.parseItemFromString(entry.getItem());
        if (stack == null) {
            return null;
        }
        stack.setItemDamage(entry.getMeta());
        stack.stackSize = entry.getAmount();
        return stack;
    }

    public static ItemStack[] getItemStacks(ItemEntry[] entries) {
        ItemStack[] stacks = new ItemStack[entries.length];
        for (int i = 0; i < entries.length; i++) {
            stacks[i] = getItemStack(entries[i]);
            if (stacks[i] == null) {
                return null;
            }
        }
        return stacks;
    }
}
