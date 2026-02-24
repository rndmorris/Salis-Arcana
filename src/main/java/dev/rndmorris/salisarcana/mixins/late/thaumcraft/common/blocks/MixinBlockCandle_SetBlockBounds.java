package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockCandle;

@Mixin(BlockCandle.class)
public abstract class MixinBlockCandle_SetBlockBounds extends Block {

    protected MixinBlockCandle_SetBlockBounds(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void setBounds(CallbackInfo ci) {
        this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.5F, 0.625F);
    }
}
