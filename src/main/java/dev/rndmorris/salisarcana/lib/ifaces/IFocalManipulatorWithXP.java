package dev.rndmorris.salisarcana.lib.ifaces;

import net.minecraft.entity.player.EntityPlayer;

public interface IFocalManipulatorWithXP {

    void salisArcana$transferXpToPlayer(EntityPlayer player);

    void salisArcana$connectPlayer(EntityPlayer player);

    void salisArcana$disconnectPlayer(EntityPlayer player);

    void salisArcana$addXP(int xp);

    void salisArcana$prioritizePlayer(EntityPlayer player);
}
