package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import dev.rndmorris.salisarcana.lib.EventUtils;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusPortableHole;

@Mixin(value = ItemFocusPortableHole.class, remap = false)
abstract class MixinItemFocusPortableHole_TriggerEvents {

    @Definition(
        id = "getBlockHardness",
        method = "Lnet/minecraft/block/Block;getBlockHardness(Lnet/minecraft/world/World;III)F",
        remap = true)
    @Expression("?.getBlockHardness(?, ?, ?, ?) != -1.0")
    @ModifyExpressionValue(method = "createHole", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean checkBreakPermission(boolean original, World world, int x, int y, int z,
        @Share("snapshot") LocalRef<BlockSnapshot> snapshotRef) {
        final EntityPlayer owner = EventUtils.portableHoleOwner;
        if (original && owner != null) {
            // Player is present, all prior checks passed, fire block-break event.
            if (EventUtils.canBreakBlock(world, x, y, z, owner)) {
                // Create snapshot for block-place event.
                snapshotRef.set(BlockSnapshot.getBlockSnapshot(world, x, y, z, 0));
                return true;
            } else {
                // Event was rejected, do not create hole.
                return false;
            }
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(
        method = "createHole",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            ordinal = 1,
            remap = true))
    private static boolean checkPlacePermission(boolean couldPlace, World world,
        @Share("snapshot") LocalRef<BlockSnapshot> snapshotRef, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        if (couldPlace && EventUtils.portableHoleOwner != null) {
            final BlockSnapshot snapshot = snapshotRef.get();
            final var event = new BlockEvent.PlaceEvent(snapshot, snapshot.replacedBlock, EventUtils.portableHoleOwner);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                // Place event blocked, roll back world.
                world.restoringBlockSnapshots = true;
                snapshot.restore(true, false);
                world.restoringBlockSnapshots = false;

                // Cancel update & sparkle, return "failed to make hole"
                cir.setReturnValue(false);
                return false;
            }
        }

        return couldPlace;
    }

    @WrapOperation(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getBlock(III)Lnet/minecraft/block/Block;",
            remap = true))
    private Block preCheckBreakPermission(World world, int x, int y, int z, Operation<Block> original,
        @Local(argsOnly = true) EntityPlayer player) {
        if (!EventUtils.canBreakBlock(world, x, y, z, player)) {
            return Blocks.bedrock; // Is hardcoded to always block Portable Holes.
        }

        return original.call(world, x, y, z);
    }

    @ModifyArg(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/ItemWandCasting;consumeAllVis(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/AspectList;ZZ)Z"),
        index = 3)
    private boolean dontSpendVisImmediately(boolean doit) {
        return false;
    }

    @WrapOperation(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusPortableHole;createHole(Lnet/minecraft/world/World;IIIIBI)Z"))
    private boolean spendVisIfCouldMakeHole(World world, int x, int y, int z, int side, byte count, int dur,
        Operation<Boolean> original, ItemStack wandStack, World world2, EntityPlayer player, @Local AspectList cost,
        @Cancellable CallbackInfoReturnable<ItemStack> cir) {

        boolean madeHole;
        EventUtils.portableHoleOwner = player;
        try {
            madeHole = original.call(world, x, y, z, side, count, dur);
        } finally {
            EventUtils.portableHoleOwner = null;
        }

        if (madeHole) {
            ((ItemWandCasting) wandStack.getItem()).consumeAllVis(wandStack, player, cost, true, false);
        } else {
            // Don't swing wand or play a sound
            cir.setReturnValue(wandStack);
        }

        return madeHole;
    }
}
