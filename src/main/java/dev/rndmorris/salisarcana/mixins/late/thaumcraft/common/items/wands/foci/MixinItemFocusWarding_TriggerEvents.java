package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.lib.EventUtils;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusWarding;

@Mixin(value = ItemFocusWarding.class, remap = false)
abstract class MixinItemFocusWarding_TriggerEvents {

    @Shadow
    public abstract AspectList getVisCost(ItemStack itemstack);

    @ModifyArg(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/items/wands/ItemWandCasting;consumeAllVis(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/AspectList;ZZ)Z"),
        index = 3)
    private boolean dontConsumeVisNow(boolean doit) {
        return false;
    }

    @WrapOperation(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlock(IIILnet/minecraft/block/Block;II)Z",
            remap = true))
    private boolean checkedSetBlock(World world, int x, int y, int z, Block block, int meta, int flags,
        Operation<Boolean> original, ItemStack wandStack, @Local(argsOnly = true) EntityPlayer player,
        @Cancellable CallbackInfoReturnable<ItemStack> cir) {

        if (!EventUtils.canBreakBlock(world, x, y, z, player)) {
            cir.setReturnValue(wandStack);
            return false;
        }

        if (!EventUtils.tryPlaceBlock(world, x, y, z, block, meta, flags, player)) {
            cir.setReturnValue(wandStack);
            return false;
        }

        if (block == ConfigBlocks.blockWarded) {
            ((ItemWandCasting) wandStack.getItem())
                .consumeAllVis(wandStack, player, this.getVisCost(wandStack), true, false);
        }

        return true;
    }
}
