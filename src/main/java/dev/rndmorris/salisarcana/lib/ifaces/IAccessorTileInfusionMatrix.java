package dev.rndmorris.salisarcana.lib.ifaces;

import java.util.ArrayList;

import net.minecraft.util.ChunkCoordinates;

public interface IAccessorTileInfusionMatrix {

    void invokeGetSurroundings();

    ArrayList<ChunkCoordinates> getPedestals();
}
