package dev.rndmorris.salisarcana.mixins.late.thaumcraft.client.lib;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public abstract class MixinRenderEventHandler_ElementalPickOredict {

    @WrapOperation(
        method = "startScan",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;",
            remap = true))
    private Block preFilterBlock(World world, int x, int y, int z, Operation<Block> original,
        @Share("includeBlock") LocalBooleanRef includeBlockRef) {
        final var block = original.call(world, x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);
        final var oreIds = OreDictionary.getOreIDs(new ItemStack(block, 1, metadata));
        final var oreDictIds = SalisArcana.proxy.oreDictIds;

        var includeBlock = false;
        for (var id : oreIds) {
            if (id == oreDictIds.pickaxeCoreScanExclude) {
                // this will cause TC to skip over the block entirely
                return Blocks.air;
            }
            if (id == oreDictIds.pickaxeCoreScanInclude) {
                includeBlock = true;
                // intentionally don't break the loop on the off chance someone screwed up and
                // the exclude tag, which should always have precedence, is in the list
            }
        }
        includeBlockRef.set(includeBlock);
        return block;
    }

    @ModifyExpressionValue(
        method = "startScan",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/oredict/OreDictionary;getOreIDs(Lnet/minecraft/item/ItemStack;)[I"))
    private int[] wrapOreIds(int[] original, @Share("includeBlock") LocalBooleanRef includeBlockRef,
        @Share("cachedArray") LocalRef<int[]> cachedArrayRef) {
        if (!includeBlockRef.get()) {
            return original;
        }
        var cachedArray = cachedArrayRef.get();
        if (cachedArray == null) {
            // because we cheat below and can force `ore` to be true, we don't
            // actually want to loop through the oredict ids if we can avoid it
            cachedArrayRef.set(cachedArray = new int[] {});
        }
        return cachedArray;
    }

    @ModifyExpressionValue(method = "startScan", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 0))
    private int setOreToTrue(int original, @Share("includeBlock") LocalBooleanRef includeBlockRef,
        @Share("block") LocalRef<Block> blockRef) {
        // set `boolean ore` to `true` if it was flagged earlier
        return includeBlockRef.get() ? 1 : original;
    }
}
