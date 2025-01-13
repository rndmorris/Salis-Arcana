package dev.rndmorris.salisarcana.mixins.late.items;

import net.minecraft.block.Block;
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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.rndmorris.salisarcana.network.MessageScanContainer;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.items.relics.ItemThaumometer;

@Mixin(value = ItemThaumometer.class, remap = false)
public class MixinItemThaumometer extends Item {

    @Inject(
        method = "onUsingTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;stopUsingItem()V"))
    private void mixinOnUsingTick(ItemStack stack, EntityPlayer p, int count, CallbackInfo ci) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(p.worldObj, p, true);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            TileEntity tile = p.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if (tile instanceof IInventory) {
                NetworkHandler.instance.sendToServer(new MessageScanContainer(mop.blockX, mop.blockY, mop.blockZ));
            }
        }
    }

    @WrapOperation(
        method = "doScan",
        at = @At(
            value = "INVOKE",
            target = "Lthaumcraft/common/lib/research/ScanManager;isValidScanTarget(Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/research/ScanResult;Ljava/lang/String;)Z",
            ordinal = 2))
    private boolean rescanInventory(EntityPlayer list, ScanResult item, String t, Operation<Boolean> original) {
        Block block = Block.getBlockFromItem(Item.getItemById(item.id));
        if (block != null && block.hasTileEntity(item.meta)) {
            TileEntity tile = block.createTileEntity(list.worldObj, item.meta);
            if (tile instanceof IInventory) {
                tile.invalidate();
                return true;
            }
        }
        return original.call(list, item, t);
    }
}
