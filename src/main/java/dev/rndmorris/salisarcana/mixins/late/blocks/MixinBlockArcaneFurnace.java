package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import thaumcraft.common.blocks.BlockArcaneFurnace;

@Mixin(BlockArcaneFurnace.class)
public abstract class MixinBlockArcaneFurnace {

    @WrapMethod(method = "onEntityCollidedWithBlock")
    private void mixinOnEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity,
        Operation<Void> original) {
        if (entity.isDead) {
            return;
        }
        original.call(world, x, y, z, entity);
    }
}
