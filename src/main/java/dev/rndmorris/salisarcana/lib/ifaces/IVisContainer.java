package dev.rndmorris.salisarcana.lib.ifaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IVisContainer {

    int getMaxVis(ItemStack stack);

    int getVis(ItemStack is, Aspect aspect);

    void storeVis(ItemStack is, Aspect aspect, int amount);

    AspectList getAspectsWithRoom(ItemStack wandstack);

    AspectList getAllVis(ItemStack is);

    boolean consumeAllVis(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit, boolean crafting);

    int addVis(ItemStack is, Aspect aspect, int amount, boolean doit);

    int addRealVis(ItemStack is, Aspect aspect, int amount, boolean doit);
}
