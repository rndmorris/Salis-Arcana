package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thaumcraft.api.research.ScanResult;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.lib.research.ScanManager;

@Mixin(value = ItemThaumometer.class, remap = false)
public class MixinItemThaumometer extends Item {

    @Inject(
        method = "onUsingTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;stopUsingItem()V"))
    private void mixinOnUsingTick(ItemStack stack, EntityPlayer p, int count, CallbackInfo ci) {
        if (p.worldObj.isRemote) {
            return;
        }
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(p.worldObj, p, true);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            TileEntity tile = p.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if (tile instanceof IInventory inventory) {
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack item = inventory.getStackInSlot(i);
                    if (item == null) {
                        continue;
                    }
                    ScanResult result = new ScanResult(
                        (byte) 1,
                        Item.getIdFromItem(item.getItem()),
                        item.getItemDamage(),
                        null,
                        "");
                    if (ScanManager.isValidScanTarget(p, result, "@")
                        && !ScanManager.getScanAspects(result, p.worldObj).aspects.isEmpty()) {
                        ScanManager.completeScan(p, result, "@");
                    }
                }
            }
        }
    }
}
