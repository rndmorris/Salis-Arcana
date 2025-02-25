package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;

@Mixin(value = ItemFocusTrade.class, remap = false)
public class MixinItemFocusTrade_HarvestLevel {

    @Inject(
        method = "onFocusRightClick",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/items/wands/foci/ItemFocusTrade;getPickedBlock(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),
        locals = LocalCapture.PRINT)
    private void wrapGetPickedBlock(ItemStack itemstack, World world, EntityPlayer player,
        MovingObjectPosition movingobjectposition, CallbackInfoReturnable<ItemStack> cir,
        @Local(ordinal = 5) MovingObjectPosition mop) {
        final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
        final var block = world.getBlock(x, y, z);
        final var metadata = world.getBlockMetadata(x, y, z);
        if (block.getHarvestLevel(metadata) < ConfigModuleRoot.enhancements.equalTradeFocusHarvestLevel.getValue()) {
            cir.setReturnValue(itemstack);
            cir.cancel();
        }
    }

}
