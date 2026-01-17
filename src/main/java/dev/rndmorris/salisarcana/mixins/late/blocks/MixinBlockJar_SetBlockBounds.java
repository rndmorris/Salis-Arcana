package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockJar;

@Mixin(BlockJar.class)
public abstract class MixinBlockJar_SetBlockBounds extends BlockContainer {

    protected MixinBlockJar_SetBlockBounds(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setBounds(CallbackInfo ci) {
        this.setBlockBounds(0.1875f, 0.0f, 0.1875f, 0.8125f, 0.75f, 0.8125f);
    }

}
