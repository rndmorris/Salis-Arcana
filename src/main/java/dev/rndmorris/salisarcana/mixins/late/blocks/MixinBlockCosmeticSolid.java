package dev.rndmorris.salisarcana.mixins.late.blocks;

import static dev.rndmorris.salisarcana.config.ConfigModuleRoot.bugfixes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.blocks.BlockCosmeticSolid;

@Mixin(value = BlockCosmeticSolid.class, remap = false)
public abstract class MixinBlockCosmeticSolid extends Block {

    protected MixinBlockCosmeticSolid(Material materialIn) {
        super(materialIn);
    }

    @WrapMethod(method = "isBeaconBase")
    public boolean onIsBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ,
        Operation<Boolean> cir) {
        final var metadata = worldObj.getBlockMetadata(x, y, z);

        return bugfixes.beaconBlockFixSetting.isBeaconMetadata(metadata);
    }

}
