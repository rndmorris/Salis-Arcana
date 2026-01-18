package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockLoot;

@Mixin(BlockLoot.class)
public abstract class MixinBlockLoot_SetBlockBounds extends Block {

    protected MixinBlockLoot_SetBlockBounds(Material materialIn) {
        super(materialIn);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setBounds(Material mat, String ip, int rt, CallbackInfo ci) {
        if (rt == 1) {
            this.setBlockBounds(0.125f, 0.0625f, 0.125f, 0.875f, 0.8125f, 0.875f);
        } else {
            this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.875f, 0.9375f);
        }
    }
}
