package dev.rndmorris.salisarcana.api;

import net.minecraft.world.World;

import thaumcraft.api.crafting.IInfusionStabiliser;

public interface IVariableInfusionStabilizer extends IInfusionStabiliser {

    /**
     * The strength of this stabilizer. Only called if {@link IVariableInfusionStabilizer#canStabaliseInfusion} returns
     * {@code true}.
     * 
     * @param world The world in which the stabilizer exists.
     * @param x     The stabilizer's x coordinate.
     * @param y     The stabilizer's y coordinate.
     * @param z     The stabilizer's z coordinate.
     * @return The stabilizer's strength.
     */
    int getStabilizerStrength(World world, int x, int y, int z);

}
