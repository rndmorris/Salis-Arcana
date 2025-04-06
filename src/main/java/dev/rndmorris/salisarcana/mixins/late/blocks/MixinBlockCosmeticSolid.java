package dev.rndmorris.salisarcana.mixins.late.blocks;

import static dev.rndmorris.salisarcana.config.SalisConfig.bugfixes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.blocks.BlockCosmeticSolid;

@Mixin(BlockCosmeticSolid.class)
public abstract class MixinBlockCosmeticSolid extends Block {

    protected MixinBlockCosmeticSolid(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "isBeaconBase", at = @At("HEAD"), cancellable = true, remap = false)
    public void onIsBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ,
        CallbackInfoReturnable<Boolean> cir) {
        final var metadata = worldObj.getBlockMetadata(x, y, z);

        cir.setReturnValue(bugfixes.beaconBlockFixSetting.isBeaconMetadata(metadata));
        cir.cancel();
    }
}
