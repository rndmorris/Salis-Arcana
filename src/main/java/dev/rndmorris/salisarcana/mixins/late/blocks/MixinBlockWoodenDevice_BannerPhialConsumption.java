package dev.rndmorris.salisarcana.mixins.late.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.rndmorris.salisarcana.config.SalisConfig;
import thaumcraft.common.blocks.BlockWoodenDevice;
import thaumcraft.common.config.ConfigItems;

@Mixin(BlockWoodenDevice.class)
public class MixinBlockWoodenDevice_BannerPhialConsumption {

    @WrapOperation(
        method = "onBlockActivated",
        at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;stackSize:I", opcode = Opcodes.PUTFIELD))
    public void removePhial(ItemStack instance, int value, Operation<Void> original,
        @Local(argsOnly = true) EntityPlayer player) {
        if (SalisConfig.bugfixes.bannerPhialConsumption.consumeEssentia()
            && !(SalisConfig.features.stopCreativeModeItemConsumption.isEnabled()
                && player.capabilities.isCreativeMode)) {
            original.call(instance, value); // Remove one filled phial

            // Add one empty phial to the inventory
            ItemStack emptyPhial = new ItemStack(ConfigItems.itemEssence, 1, 0);
            if (!player.inventory.addItemStackToInventory(emptyPhial)) {
                player.dropPlayerItemWithRandomChoice(emptyPhial, false);
            }

            player.inventoryContainer.detectAndSendChanges();
        }
    }
}
