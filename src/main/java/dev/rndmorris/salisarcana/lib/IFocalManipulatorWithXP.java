package dev.rndmorris.salisarcana.lib;

import net.minecraft.entity.player.EntityPlayer;

public interface IFocalManipulatorWithXP {

    void salisArcana$transferXpToPlayer(EntityPlayer player);

    void salisArcana$connectPlayer(EntityPlayer player);

    void salisArcana$disconnectPlayer(EntityPlayer player);
}
