package dev.rndmorris.salisarcana.mixins.late.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import thaumcraft.common.lib.events.ServerTickEventsFML;

/**
 * Accessor for the package local values of {@link ServerTickEventsFML.VirtualSwapper}
 */
@Mixin(ServerTickEventsFML.VirtualSwapper.class)
public interface MixinServerTickEventsFML_VirtualSwapper {

    /**
     * Get the swapper lifespan for this block.
     * Reduces by 1 for every subsequent block that is replaced during a swap event until it reaches 0
     */
    @Accessor(remap = false)
    int getLifespan();

    /**
     * Get the block's X coordinate
     */
    @Accessor(remap = false)
    int getX();

    /**
     * Get the block's Y coordinate
     */
    @Accessor(remap = false)
    int getY();

    /**
     * Get the block's Z coordinate
     */
    @Accessor(remap = false)
    int getZ();

    /**
     * Get the class for the block currently in world
     */
    @Accessor(remap = false)
    Block getBSource();

    /**
     * Get the metadata for the block currently in world
     */
    @Accessor(remap = false)
    int getMSource();

    /**
     * Get the ItemStack for the block to be swapped in, from the player's inventory
     */
    @Accessor(remap = false)
    ItemStack getTarget();

    /**
     * Get the Wand used for this swap event
     */
    @Accessor(remap = false)
    int getWand();

    /**
     * Get the player for this swap event
     */
    @Accessor(remap = false)
    EntityPlayer getPlayer();
}
