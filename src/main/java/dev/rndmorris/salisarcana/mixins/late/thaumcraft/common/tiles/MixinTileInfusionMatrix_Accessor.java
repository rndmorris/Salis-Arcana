package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.tiles;

import java.util.ArrayList;

import net.minecraft.util.ChunkCoordinates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import dev.rndmorris.salisarcana.lib.ifaces.IAccessorTileInfusionMatrix;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
abstract class MixinTileInfusionMatrix_Accessor implements IAccessorTileInfusionMatrix {

    @Invoker("getSurroundings")
    public abstract void invokeGetSurroundings();

    @Accessor("pedestals")
    public abstract ArrayList<ChunkCoordinates> getPedestals();
}
