package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.events;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;

import dev.rndmorris.salisarcana.lib.EventUtils;
import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(value = ServerTickEventsFML.class, remap = false)
abstract class MixinServerTickEventsFML_EqualTradeEvents {

    @WrapMethod(method = "tickBlockSwap")
    private void repeatTickBlockSwap(World world, Operation<Void> original,
        @Share("cancelled") LocalBooleanRef cancelled) {
        // This method includes a loop that repeats until a block gets mutated. Since we're forced to early-return from
        // a cancelled BlockEvent.PlaceEvent in order to avoid spending vis and triggering SFX, we use this method to
        // catch these early returns and send the loop in for another go, so we always perform an operation each tick.

        do {
            cancelled.set(false);
            original.call(world);
        } while (cancelled.get());
    }

    @Definition(id = "slot", local = @Local(type = int.class, name = "slot"))
    @Expression("slot >= 0")
    @ModifyExpressionValue(method = "tickBlockSwap", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean fireBlockBreakEvent(boolean original, World world, @Local ServerTickEventsFML.VirtualSwapper vs,
        @Local Block bi, @Local(name = "md") int metadata) {
        final var avs = (AccessorVirtualSwapper) vs;
        return original && EventUtils.canBreakBlock(world, avs.x(), avs.y(), avs.z(), bi, metadata, avs.player());
    }

    @WrapOperation(
        method = "tickBlockSwap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/world/World;IIIII)Ljava/util/ArrayList;"))
    private ArrayList<ItemStack> fireBlockHarvestEvent(Block instance, World world, int x, int y, int z, int metadata,
        int fortune, Operation<ArrayList<ItemStack>> original, @Local ServerTickEventsFML.VirtualSwapper vs) {
        ArrayList<ItemStack> drops = original.call(instance, world, x, y, z, metadata, fortune);
        final var avs = (AccessorVirtualSwapper) vs;
        ForgeEventFactory
            .fireBlockHarvesting(drops, world, instance, x, y, z, metadata, fortune, 1f, false, avs.player());
        return drops;
    }

    @Inject(
        method = "tickBlockSwap",
        at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void fireBlockSilkTouchEvent(World world, CallbackInfo ci, @Local ServerTickEventsFML.VirtualSwapper vs,
        @Local ArrayList<ItemStack> drops, @Local Block bi, @Local(name = "md") int metadata) {
        final var avs = (AccessorVirtualSwapper) vs;
        ForgeEventFactory
            .fireBlockHarvesting(drops, world, bi, avs.x(), avs.y(), avs.z(), metadata, 0, 1f, true, avs.player());
    }

    @ModifyReceiver(method = "tickBlockSwap", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;size()I"))
    private ArrayList<ItemStack> tryPlaceBlockEvent(ArrayList<ItemStack> drops, World world,
        @Local ServerTickEventsFML.VirtualSwapper vs, @Share("cancelled") LocalBooleanRef cancelled,
        @Cancellable CallbackInfo ci) {
        final var avs = (AccessorVirtualSwapper) vs;
        // spotless:off
        final Block block = Block.getBlockFromItem(avs.target().getItem());
        final int meta = avs.target().getItemDamage();
        // spotless:on

        if (!EventUtils.tryPlaceBlock(world, avs.x(), avs.y(), avs.z(), block, meta, 3, avs.player())) {
            cancelled.set(true);
            ci.cancel();
        }

        return drops;
    }

    @WrapOperation(
        method = "tickBlockSwap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            remap = true))
    private boolean tryPlaceBlockCreative(World world, int x, int y, int z, Block block, int metadata, int flags,
        Operation<Boolean> original, @Local ServerTickEventsFML.VirtualSwapper vs,
        @Share("cancelled") LocalBooleanRef cancelled, @Cancellable CallbackInfo ci) {
        final var avs = (AccessorVirtualSwapper) vs;
        if (avs.player().capabilities.isCreativeMode) {
            // In Creative Mode, tryPlaceBlockEvent is skipped, so we try to place the block here instead.
            if (!EventUtils.tryPlaceBlock(world, avs.x(), avs.y(), avs.z(), block, metadata, flags, avs.player())) {
                cancelled.set(true);
                ci.cancel();
                return false;
            }
        }

        // In Survival Mode, we already placed the block during tryPlaceBlockEvent.
        return true;
    }

    @Mixin(value = ServerTickEventsFML.VirtualSwapper.class, remap = false)
    interface AccessorVirtualSwapper {

        @Accessor("player")
        EntityPlayer player();

        @Accessor("x")
        int x();

        @Accessor("y")
        int y();

        @Accessor("z")
        int z();

        @Accessor("target")
        ItemStack target();
    }
}
