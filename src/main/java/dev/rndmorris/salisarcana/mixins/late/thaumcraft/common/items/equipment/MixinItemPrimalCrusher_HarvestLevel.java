package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.equipment;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import thaumcraft.common.items.equipment.ItemPrimalCrusher;

@Mixin(value = ItemPrimalCrusher.class, remap = false)
public class MixinItemPrimalCrusher_HarvestLevel {

    @WrapOperation(
        method = "onBlockDestroyed",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getBlockHardness(Lnet/minecraft/world/World;III)F"),
        remap = true)
    private float wrapIsEffectiveAgainst(Block instance, World world, int x, int y, int z, Operation<Float> original,
        @Local(name = "md") int meta) {
        return ItemPrimalCrusher.material.getHarvestLevel() >= instance.getHarvestLevel(meta)
            ? original.call(instance, world, x, y, z)
            : -1F;
    }
}
