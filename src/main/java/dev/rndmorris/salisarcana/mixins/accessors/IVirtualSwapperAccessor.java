package dev.rndmorris.salisarcana.mixins.accessors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Accessor for the package local values of ServerTickEventsFML.VirtualSwapper
 */
public interface IVirtualSwapperAccessor {

    /**
     * Get the swapper lifespan for this block.
     * Reduces by 1 for every subsequent block that is replaced during a swap event until it reaches 0
     */
    int sa$getLifespan();

    /**
     * Get the block's X coordinate
     */
    int sa$getX();

    /**
     * Get the block's Y coordinate
     */
    int sa$getY();

    /**
     * Get the block's Z coordinate
     */
    int sa$getZ();

    /**
     * Get the class for the block currently in world
     */
    Block sa$getSourceBlock();

    /**
     * Get the metadata for the block currently in world
     */
    int sa$getSourceMetadata();

    /**
     * Get the ItemStack for the block to be swapped in, from the player's inventory
     */
    ItemStack sa$getTarget();

    /**
     * Get the Wand used for this swap event
     */
    int sa$getWand();

    /**
     * Get the player for this swap event
     */
    EntityPlayer sa$getPlayer();
}
