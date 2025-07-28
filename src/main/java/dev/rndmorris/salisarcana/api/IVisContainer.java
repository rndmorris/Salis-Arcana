package dev.rndmorris.salisarcana.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * Interface for items that can store vis, such as wands and amulets.
 * <p>
 * This interface allows for the management of vis storage, retrieval, and consumption.
 * By default, this interface is implemented by {@link thaumcraft.common.items.wands.ItemWandCasting} and
 * {@link thaumcraft.common.items.baubles.ItemAmuletVis}.
 * This avoids redundant code when working with both wands and amulets.
 */
public interface IVisContainer {

    /**
     * Gets the maximum amount of vis that can be stored in the given ItemStack.
     *
     * @param stack The ItemStack to check.
     * @return The maximum vis capacity of the ItemStack.
     */
    int getMaxVis(ItemStack stack);

    /**
     * Gets the current amount of vis stored in the given ItemStack.
     *
     * @param is     The ItemStack to check.
     * @param aspect The Aspect of vis to retrieve.
     * @return The amount of the specified Aspect stored in the ItemStack.
     */
    int getVis(ItemStack is, Aspect aspect);

    /**
     * Stores a specific amount of vis in the given ItemStack.
     *
     * @param is     The ItemStack to modify.
     * @param aspect The Aspect of vis to store.
     * @param amount The amount of vis to store.
     */
    void storeVis(ItemStack is, Aspect aspect, int amount);

    /**
     * Gets a list of all Aspects stored in the given ItemStack that have room for more vis.
     *
     * @param is The ItemStack to check.
     * @return An AspectList containing all aspects that have room for more vis.
     */
    AspectList getAspectsWithRoom(ItemStack is);

    /**
     * Gets a list of all Aspects currently stored in the given ItemStack.
     *
     * @param is The ItemStack to check.
     * @return An AspectList containing all aspects stored in the ItemStack.
     */
    AspectList getAllVis(ItemStack is);

    /**
     * Consumes vis according to the specified AspectList.
     *
     * @param is       the ItemStack to consume vis from
     * @param player   the player consuming the vis
     * @param aspects  the AspectList containing the aspects and amounts to consume
     * @param doit     if true, the vis will be consumed; if false, it will only check if consumption is possible
     * @param crafting if true, this is for crafting purposes and may have different rules for consumption
     * @return true if all aspects were successfully consumed, false otherwise
     */
    boolean consumeAllVis(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit, boolean crafting);

    /**
     * Adds a specific amount of vis to the given ItemStack.
     * 
     * @param is     the ItemStack to modify
     * @param aspect the Aspect of vis to add
     * @param amount the amount of vis to add
     * @param doit   if true, the vis will be added; if false, it will only check if there is room
     * @return the amount of leftover vis
     */
    int addVis(ItemStack is, Aspect aspect, int amount, boolean doit);

    /**
     * Adds a specific amount of vis to an ItemStack, converting from Centivis to Vis.
     * 
     * @param is     the ItemStack to modify
     * @param aspect the Aspect of vis to add
     * @param amount the amount of vis to add, in Centivis
     * @param doit   if true, the vis will be added; if false, it will only check if there is room
     * @return the amount of leftover vis
     */
    int addRealVis(ItemStack is, Aspect aspect, int amount, boolean doit);
}
