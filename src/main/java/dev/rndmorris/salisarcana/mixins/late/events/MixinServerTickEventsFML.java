package dev.rndmorris.salisarcana.mixins.late.events;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.mixins.accessors.IVirtualSwapperAccessor;
import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(ServerTickEventsFML.class)
public abstract class MixinServerTickEventsFML {

    @ModifyExpressionValue(
        method = "tickBlockSwap",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItemDamage()I"))
    private static int injectLeavesMetadata(int originalMeta, World world,
        @Local(ordinal = 0) ServerTickEventsFML.VirtualSwapper vs) {
        IVirtualSwapperAccessor swapper = (IVirtualSwapperAccessor) vs;
        Block targetBlock = Block.getBlockFromItem(
            swapper.sa$getTarget()
                .getItem());

        // set leaf 'persistent' flag
        return targetBlock.isLeaves(world, swapper.sa$getX(), swapper.sa$getY(), swapper.sa$getZ()) ? originalMeta | 4
            : originalMeta;
    }
}
