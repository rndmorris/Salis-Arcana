package dev.rndmorris.salisarcana.mixins.late.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.ConfigModuleRoot;
import dev.rndmorris.salisarcana.network.MessageScanIInventory;
import dev.rndmorris.salisarcana.network.NetworkHandler;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;

@Mixin(value = ScanManager.class, remap = false)
public class MixinScanManager {

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
        if (ConfigModuleRoot.enhancements.thaumometerScanContainersResearch.isEnabled()
            && !ResearchManager.isResearchComplete(
                player.getCommandSenderName(),
                ConfigModuleRoot.enhancements.thaumometerScanContainersResearch.researchName)) {
            return;
        }
        Block block = Block.getBlockFromItem(Item.getItemById(scan.id));
        if (block != null && block.hasTileEntity(scan.meta)) {
            TileEntity tile = block.createTileEntity(player.worldObj, scan.meta);
            if (tile != null) { // gt machines can return null here
                tile.invalidate();
                if (tile instanceof IInventory && !ScanManager.isValidScanTarget(player, scan, "@")) {
                    if (player.isClientWorld()) {
                        NetworkHandler.instance.sendToServer(new MessageScanIInventory(scan.id, scan.meta));
                    }
                    cir.cancel();
                }
            }
        }
    }
}
