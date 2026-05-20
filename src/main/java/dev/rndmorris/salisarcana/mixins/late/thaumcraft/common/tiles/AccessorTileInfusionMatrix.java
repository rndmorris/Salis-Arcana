package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.util.ChunkCoordinates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
public interface AccessorTileInfusionMatrix {

    @Invoker("getSurroundings")
    void invokeGetSurroundings();

    @Accessor("pedestals")
    ArrayList<ChunkCoordinates> getPedestals();
}
