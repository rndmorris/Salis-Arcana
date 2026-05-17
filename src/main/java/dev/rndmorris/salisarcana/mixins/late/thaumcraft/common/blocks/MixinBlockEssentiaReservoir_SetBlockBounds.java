package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockEssentiaReservoir;

@Mixin(BlockEssentiaReservoir.class)
public abstract class MixinBlockEssentiaReservoir_SetBlockBounds extends BlockContainer {

    protected MixinBlockEssentiaReservoir_SetBlockBounds(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void setBounds(CallbackInfo ci) {
        this.setBlockBounds(0.125f, 0.125f, 0.125f, 0.875f, 0.875f, 0.875f);
    }
}
