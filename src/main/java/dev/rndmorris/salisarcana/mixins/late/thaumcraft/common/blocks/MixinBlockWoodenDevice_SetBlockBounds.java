package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockWoodenDevice;

@Mixin(BlockWoodenDevice.class)
abstract class MixinBlockWoodenDevice_SetBlockBounds extends BlockContainer {

    protected MixinBlockWoodenDevice_SetBlockBounds(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "setBlockBoundsBasedOnState", at = @At("HEAD"))
    private void setBoundsBasedOnState(IBlockAccess world, int x, int y, int z, CallbackInfo ci) {
        int md = world.getBlockMetadata(x, y, z);
        // 1 = Arcane Ear
        // 2, 3 = Arcane Pressure Plate
        // 4 = Arcane Bore Base
        // 5 = Banner
        // 6 = Greatwood Planks
        // 7 = Silverwood Planks
        if (md != 1 && md != 2 && md != 3 && md != 5) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
