package dev.rndmorris.salisarcana.mixins.late.thaumcraft.common.lib.research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.SalisConfig;
import dev.rndmorris.salisarcana.network.MessageScanIInventory;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

@Mixin(value = ScanManager.class, remap = false)
public class MixinScanManager_ScanContainers {

    @Inject(
        method = "completeScan",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lthaumcraft/common/lib/crafting/ThaumcraftCraftingManager;getObjectTags(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;",
            ordinal = 0),
        cancellable = true)
    private static void onScanComplete(EntityPlayer player, ScanResult scan, String prefix,
        CallbackInfoReturnable<Boolean> cir, @Local AspectList aspects) {
        if (!player.worldObj.isRemote) {
            return;
        }
        if (SalisConfig.features.thaumometerScanContainersResearch.isEnabled() && !ResearchManager.isResearchComplete(
            player.getCommandSenderName(),
            SalisConfig.features.thaumometerScanContainersResearch.getInternalName())) {
            return;
        }

        // Items.feather is just a simple way to get access to getMovingObjectPositionFromPlayer
        MovingObjectPosition mop = Items.feather.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        if (mop == null) {
            return;
        }
        TileEntity tile = player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
        // null check is handled by instanceof
        if (tile instanceof IInventory && !ScanManager.isValidScanTarget(player, scan, "@")) {
            if (player.isClientWorld()) {
                NetworkHandler.instance.sendToServer(new MessageScanIInventory(scan.id, scan.meta));
            }
            cir.setReturnValue(false);
        }
    }
}
