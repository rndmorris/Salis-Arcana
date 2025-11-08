package dev.rndmorris.salisarcana.mixins.late.lib.events;

import static thaumcraft.common.lib.events.ServerTickEventsFML.VirtualSwapper;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.events.ServerTickEventsFML;

@Mixin(ServerTickEventsFML.class)
public class MixinServerTickEventsFML_EqualTrade {

    @WrapOperation(method = "tickBlockSwap", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;size()I"))
    private int captureDrops(ArrayList<ItemStack> instance, Operation<Integer> original,
        @Share("drops") LocalRef<ArrayList<ItemStack>> dropsRef) {
        dropsRef.set(instance);
        return 0;
    }

    @WrapOperation(
        method = "tickBlockSwap",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/ItemWandCasting;consumeAllVis(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/AspectList;ZZ)Z"))
    private boolean captureConsumeVis(ItemWandCasting wand, ItemStack wandStack, EntityPlayer player, AspectList price,
        boolean doit, boolean crafting, Operation<Boolean> original, @Share("wand") LocalRef<ItemStack> wandRef,
        @Share("price") LocalRef<AspectList> priceRef) {

        wandRef.set(wandStack);
        priceRef.set(price);

        return false;
    }

    @WrapOperation(
        method = "tickBlockSwap",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z"))
    public boolean callOnBlockPlaced(World world, int x, int y, int z, Block block, int meta, int flags,
        Operation<Boolean> original, @Share("meta") LocalIntRef metaRef, @Local VirtualSwapper vs) {
        final IVirtualSwapperAccessor swapper = (IVirtualSwapperAccessor) vs;
        ForgeDirection bestSide = ForgeDirection.UP;
        int maxScore = Integer.MIN_VALUE;

        for (final var side : ForgeDirection.VALID_DIRECTIONS) {
            final var score = sa$getSidePriority(world, x, y, z, side, swapper.getBSource(), swapper.getMSource());
            if (score >= maxScore) {
                maxScore = score;
                bestSide = side;
            }
        }

        // TODO Fix direction choosing logic.
        System.out.println("Chosen side: " + bestSide.name());

        meta = block.onBlockPlaced(
            world,
            x,
            y,
            z,
            bestSide.ordinal(),
            (bestSide.offsetX + 1) / 2f,
            (bestSide.offsetY + 1) / 2f,
            (bestSide.offsetZ + 1) / 2f,
            meta);

        metaRef.set(meta);
        return original.call(world, x, y, z, block, meta, flags);
    }

    @WrapOperation(
        method = "tickBlockSwap",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;onBlockPlacedBy(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V"))
    public void callOnPostBlockPlaced(Block block, World worldIn, int x, int y, int z, EntityLivingBase placer,
        ItemStack itemIn, Operation<Void> original, @Share("meta") LocalIntRef metaRef, @Local VirtualSwapper vs,
        @Share("drops") LocalRef<ArrayList<ItemStack>> dropsRef, @Share("wand") LocalRef<ItemStack> wandRef,
        @Share("price") LocalRef<AspectList> priceRef, @Cancellable CallbackInfo ci) {

        original.call(block, worldIn, x, y, z, placer, itemIn);
        block.onPostBlockPlaced(worldIn, x, y, z, metaRef.get());

        // Prevent chain reactions.
        // TODO Capture block drops & vis spending.
        final var swapper = (IVirtualSwapperAccessor) vs;
        if (worldIn.getBlock(x, y, z) == swapper.getBSource()
            && worldIn.getBlockMetadata(x, y, z) == swapper.getMSource()) {
            ci.cancel();
        } else if (wandRef.get() != null) {
            final var player = swapper.getPlayer();
            for (ItemStack drop : dropsRef.get()) {
                if (!player.inventory.addItemStackToInventory(drop)) {
                    worldIn.spawnEntityInWorld(
                        new EntityItem(worldIn, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, drop));
                }
            }

            final var wandStack = wandRef.get();
            final var wand = (ItemWandCasting) wandStack.getItem();

            // noinspection DataFlowIssue
            wand.consumeAllVis(wandStack, player, priceRef.get(), true, false);
        }
    }

    @Unique
    private int sa$getSidePriority(World world, int x, int y, int z, ForgeDirection dir, Block target, int meta) {
        final int dx = x - dir.offsetX;
        final int dy = y - dir.offsetY;
        final int dz = z - dir.offsetZ;

        int score = 0;

        if (world.getBlock(dx, dy, dz) == target && world.getBlockMetadata(dx, dy, dz) == meta) {
            score -= 100;
        } else if (world.isSideSolid(dx, dy, dz, dir)) {
            score += 50;
        } else if (!world.isAirBlock(dx, dy, dz)) {
            score += 25;
        }

        // Check opposite side.
        final int bx = x + dir.offsetX;
        final int by = x + dir.offsetY;
        final int bz = x + dir.offsetZ;

        if (world.isAirBlock(bx, by, bz)) {
            score += 10;
        } else if (!world.isSideSolid(bx, by, bz, dir.getOpposite())) {
            score += 5;
        }

        return score;
    }
}
