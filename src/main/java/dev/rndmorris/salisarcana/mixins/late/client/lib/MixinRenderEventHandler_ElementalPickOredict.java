package dev.rndmorris.salisarcana.mixins.late.client.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.client.lib.RenderEventHandler;

@Mixin(value = RenderEventHandler.class, remap = false)
public abstract class MixinRenderEventHandler_ElementalPickOredict {

    @ModifyExpressionValue(
        method = "startScan",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;"))
    private Block preFilterBlock(Block original, @Local(argsOnly = true) Entity player, @Local(name = "x") int x,
        @Local(name = "y") int y, @Local(name = "z") int z, @Local(name = "xx") int xx, @Local(name = "yy") int yy,
        @Local(name = "zz") int zz, @Share("includeBlock") LocalBooleanRef includeBlockRef) {
        final var metadata = player.worldObj.getBlockMetadata(x + xx, y + yy, z + zz);
        final var oreIds = OreDictionary.getOreIDs(new ItemStack(original, 1, metadata));
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
        return original;
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
