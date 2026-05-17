package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.common.blocks.BlockArcaneFurnace;

@Mixin(BlockArcaneFurnace.class)
public class MixinBlockArcaneFurnace_DupeFix {

    @Inject(method = "onEntityCollidedWithBlock", at = @At(value = "HEAD"), cancellable = true)
    private void mixinOnEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity, CallbackInfo ci) {
        if (entity.isDead) {
            ci.cancel();
        }
    }
}
