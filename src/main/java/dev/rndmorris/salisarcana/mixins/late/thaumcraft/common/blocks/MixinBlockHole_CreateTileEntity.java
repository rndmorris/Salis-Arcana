package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.blocks.BlockHole;
import thaumcraft.common.tiles.TileHole;

@Mixin(value = BlockHole.class)
abstract class MixinBlockHole_CreateTileEntity {

    @WrapMethod(method = "createNewTileEntity")
    private TileEntity createTileHole(World world, int meta, Operation<TileEntity> original) {
        return new TileHole();
    }
}
