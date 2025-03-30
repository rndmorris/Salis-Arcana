package dev.rndmorris.salisarcana.common.recipes;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.blocks.BlockCosmeticWoodSlab;

/**
 * Event handler to override the burn time of TC wooden slabs.
 * {@link cpw.mods.fml.common.IFuelHandler}s are called too late in
 * {@link net.minecraft.tileentity.TileEntityFurnace#getItemBurnTime(ItemStack)}
 * to fix the problem, so we listen for the event instead, which is called before everything else.
 */
@SuppressWarnings("deprecation")
public class FuelBurnTimeEventHandler {

    @SubscribeEvent
    public void onFuelBurnTimeEvent(net.minecraftforge.event.FuelBurnTimeEvent event) {
        if (event.fuel != null && event.fuel.getItem() != null
            && BlockCosmeticWoodSlab.getBlockFromItem(event.fuel.getItem()) instanceof BlockCosmeticWoodSlab) {
            event.burnTime = 150;
            event.setResult(Event.Result.ALLOW);
        }
    }

}
