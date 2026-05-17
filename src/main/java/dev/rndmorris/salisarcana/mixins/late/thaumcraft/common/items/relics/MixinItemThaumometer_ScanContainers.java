package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.items.relics;

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
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.network.MessageScanContainer;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = ItemThaumometer.class)
public class MixinItemThaumometer_ScanContainers extends Item {

    @Inject(
        method = "onUsingTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;stopUsingItem()V"))
    private void mixinOnUsingTick(ItemStack stack, EntityPlayer player, int count, CallbackInfo ci) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            TileEntity tile = player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
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
            ordinal = 2),
        remap = false)
    private boolean rescanInventory(EntityPlayer player, ScanResult item, String t, Operation<Boolean> original,
        @Local TileEntity tile) {
        if (SalisConfig.features.thaumometerScanContainersResearch.isEnabled()) {
            if (!ResearchManager.isResearchComplete(
                player.getCommandSenderName(),
                SalisConfig.features.thaumometerScanContainersResearch.getInternalName())) {
                return original.call(player, item, t);
            }
        }
        // all other checks have already been handled
        // returns false if tile is null as well
        if (tile instanceof IInventory) {
            return true;
        }

        return original.call(player, item, t);
    }
}
