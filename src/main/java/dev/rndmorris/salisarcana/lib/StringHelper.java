package dev.rndmorris.salisarcana.lib;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public final class StringHelper {

    /**
     * Parses an ItemStack from a string in the format "modid:itemname[:metadata[:stacksize]]".
     * 
     * @param string The string to parse.
     * @return The ItemStack, or null if the item could not be found.
     */
    public static ItemStack parseItemFromString(String string) {
        String[] split = string.split(":");
        ItemStack stack = GameRegistry.findItemStack(split[0], split[1], 1);
        if (stack == null) {
            return null;
        }
        if (split.length > 2) {
            stack.setItemDamage(Integer.parseInt(split[2]));
        }
        if (split.length > 3) {
            stack.stackSize = Integer.parseInt(split[3]);
        }
        return stack;
    }

}
