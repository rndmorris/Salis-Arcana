package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.wands.foci;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import thaumcraft.common.items.wands.foci.ItemFocusPortableHole;

@Mixin(value = ItemFocusPortableHole.class)
abstract class MixinItemFocusPortableHole_Sync {

    @Inject(method = "onFocusRightClick", at = @At("HEAD"), cancellable = true, remap = false)
    private void skipClientPrediction(ItemStack itemstack, World world, EntityPlayer player,
        MovingObjectPosition mop, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isRemote) {
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
                player.swingItem();
            }
            cir.setReturnValue(itemstack);
        }
    }
}
