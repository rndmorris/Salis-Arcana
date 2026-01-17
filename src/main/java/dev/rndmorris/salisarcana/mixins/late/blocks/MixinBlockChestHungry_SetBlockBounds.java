package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockChestHungry;

@Mixin(BlockChestHungry.class)
public abstract class MixinBlockChestHungry_SetBlockBounds extends BlockContainer {

    protected MixinBlockChestHungry_SetBlockBounds(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setBounds(CallbackInfo ci) {
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }
}
