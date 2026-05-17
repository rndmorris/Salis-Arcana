package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import thaumcraft.common.blocks.BlockAiry;

@Mixin(BlockAiry.class)
public class MixinBlockAiry_EarthShockCheckSolidGround {

    @ModifyExpressionValue(
        method = "onNeighborBlockChange",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"))
    public int checkSolidGround(int meta, World world, int x, int y, int z) {
        // Spark block from Earth Shock
        if (meta == 10 && !world.isRemote && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            world.setBlockToAir(x, y, z);
        }

        return meta;
    }
}
